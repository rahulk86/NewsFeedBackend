package com.NewFeed.backend.controller;

import com.NewFeed.backend.dto.UserDto;
import com.NewFeed.backend.event.ForgetPasswordEvent;
import com.NewFeed.backend.event.RegistrationCompleteEvent;
import com.NewFeed.backend.exception.TokenRefreshException;
import com.NewFeed.backend.exception.UserVerificationException;
import com.NewFeed.backend.modal.auth.RefreshToken;
import com.NewFeed.backend.modal.user.NewFeedUser;
import com.NewFeed.backend.payload.Request.ForgetPasswordRequest;
import com.NewFeed.backend.payload.Request.LoginRequest;
import com.NewFeed.backend.payload.Request.RegisterRequest;
import com.NewFeed.backend.payload.Request.SignUpRequest;
import com.NewFeed.backend.payload.Response.LogInResponse;
import com.NewFeed.backend.payload.Response.MessageResponse;
import com.NewFeed.backend.payload.Response.VerificationTokenResponse;
import com.NewFeed.backend.security.JwtService;
import com.NewFeed.backend.service.RefreshTokenService;
import com.NewFeed.backend.service.UserForgetPasswordService;
import com.NewFeed.backend.service.UserService;
import com.NewFeed.backend.service.UserVerificationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserVerificationService userVerificationService;

    @Autowired
    private UserForgetPasswordService userForgetPasswordService;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private  ApplicationEventPublisher publisher;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    JwtService jwtService;

    @MessageMapping("/emailVerificationSuccess/{token}")
    @SendTo("/topic/emailVerificationSuccess/{token}")
    public ResponseEntity<?> sendEmailVerificationSuccess(@DestinationVariable String token) {
        return ResponseEntity.ok(token);
    }

    @GetMapping("/verify/email")
    public ResponseEntity<MessageResponse> verifyEmail(@RequestParam("token") String token) {
        try {
            userVerificationService.verifyByToken(token);
            return new ResponseEntity<>(MessageResponse.
                    builder().
                    message("verification-success").
                    build(),
                    HttpStatus.ACCEPTED);
        }catch (UserVerificationException exception){
            return new ResponseEntity<>(MessageResponse.
                    builder().
                    message(exception.getMessage()).
                    build(),
                    HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/passwordResetEmail")
    public ResponseEntity<MessageResponse> passwordResetEmail(@RequestParam("email") String email,final HttpServletRequest request) {
        try {
            UserDto userDto = (UserDto) userService.loadUserByUsername(email);
            ForgetPasswordEvent forgetPasswordEvent = new ForgetPasswordEvent(userDto, applicationUrl(request));
            publisher.publishEvent(forgetPasswordEvent);
            return new ResponseEntity<>(MessageResponse.
                    builder().
                    message("Password reset email sent successfully").
                    build(),
                    HttpStatus.ACCEPTED);
        }catch (DataIntegrityViolationException exception){
            return new ResponseEntity<>(MessageResponse.
                    builder().
                    message(exception.getMessage()).
                    build(),
                    HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/forgetPassword")
    public ResponseEntity<MessageResponse> forgetPassword( @RequestBody ForgetPasswordRequest forgetPasswordRequest) {
        try {
            userForgetPasswordService.verifyByToken(forgetPasswordRequest);
            return new ResponseEntity<>(MessageResponse.
                    builder().
                    message("Password reset successful. You can now login with your new password.").
                    build(),
                    HttpStatus.ACCEPTED);
        }catch (DataIntegrityViolationException exception){
            return new ResponseEntity<>(MessageResponse.
                    builder().
                    message(exception.getMessage()).
                    build(),
                    HttpStatus.CONFLICT);
        }
    }
    @GetMapping("/verify/emailByCode")
    public ResponseEntity<MessageResponse> verifyEmailByCode(@RequestParam("code") String confirmationCode) {
        try {
            userVerificationService.verifyByConfirmationCode(confirmationCode);
            return new ResponseEntity<>(MessageResponse.
                    builder().
                    message("verification-success").
                    build(),
                    HttpStatus.ACCEPTED);
        }catch (UserVerificationException exception){
            return new ResponseEntity<>(MessageResponse.
                    builder().
                    message(exception.getMessage()).
                    build(),
                    HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> signup(@Valid  @RequestBody SignUpRequest signUpRequest) {
        try{
            userService.signup(UserDto
                .builder()
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .build());
            return new ResponseEntity<>(MessageResponse.
                builder().
                message("signup  successfully").
                build(),
                HttpStatus.ACCEPTED);
        }
        catch (DataIntegrityViolationException e){
            return new ResponseEntity<>(MessageResponse.
                    builder().
                    message(e.getMessage()).
                    build(),
                    HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid  @RequestBody RegisterRequest registerRequest,final HttpServletRequest request){
        try {

            UserDto userDto = userService.createUser(UserDto.builder()
                                                        .email(registerRequest.getEmail())
                                                        .password(registerRequest.getPassword())
                                                        .name(registerRequest.getUsername())
                                                        .build());
            RegistrationCompleteEvent registrationCompleteEvent = new RegistrationCompleteEvent(userDto, applicationUrl(request));
            publisher.publishEvent(registrationCompleteEvent);
            return new ResponseEntity<>(VerificationTokenResponse.
                                        builder().
                                        token(registrationCompleteEvent.getVerificationToken()).
                                        message("token created successfully").
                                        build(),
                                        HttpStatus.CREATED);
        }
        catch (DataIntegrityViolationException e){
            return new ResponseEntity<>(MessageResponse.
                                        builder().
                                        message(e.getMessage()).
                                        build(),
                                        HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request){
        try {
            String refreshToken = jwtService.getJwtRefreshFromCookies(request);
            UserDto userDto = refreshTokenService.getUser(refreshToken);
            Set<String> roles = userDto
                                .getAuthorities()
                                .stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toSet());
            ResponseCookie responseCookie = jwtService.generateJwtCookie(userDto);
            return new ResponseEntity<>(LogInResponse
                    .builder()
                    .accessToken(responseCookie.getValue())
                    .roles(roles)
                    .build(),
                     HttpStatus.CREATED);
        }catch (TokenRefreshException | DataIntegrityViolationException  e){
            return new ResponseEntity<>(MessageResponse.
                    builder().
                    message(e.getMessage()).
                    build(),
                    HttpStatus.CONFLICT);
        }

    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest jwtRequest) {
       try {
           Authentication authentication = this.doAuthenticate(jwtRequest.getEmail(), jwtRequest.getPassword());
           SecurityContextHolder.getContext().setAuthentication(authentication);
           return buildLoginResponse(authentication);
       }catch (DataIntegrityViolationException e){
           return new ResponseEntity<>(MessageResponse.
                   builder().
                   message(e.getMessage()).
                   build(),
                   HttpStatus.CREATED);
       }

    }

    @PostMapping("/oauth2")
    public ResponseEntity<?> oauth2(Authentication authentication) {
        try {
            return buildLoginResponse(authentication);
        }catch (DataIntegrityViolationException e){
            return new ResponseEntity<>(MessageResponse.
                    builder().
                    message(e.getMessage()).
                    build(),
                    HttpStatus.CREATED);
        }

    }

    private ResponseEntity<?> buildLoginResponse(Authentication authentication){
        UserDto userDetails = (UserDto) authentication.getPrincipal();
        Set<String> roles   = userDetails
                                    .getAuthorities()
                                    .stream()
                                    .map(GrantedAuthority::getAuthority)
                                    .collect(Collectors.toSet());

        ResponseCookie jwtCookie        = jwtService.generateJwtCookie(userDetails);
        NewFeedUser newFeedUser         = userService.toNewFeedUser(userDetails);
        RefreshToken refreshToken       = refreshTokenService.createRefreshToken(newFeedUser);
        ResponseCookie jwtRefreshCookie = jwtService.generateRefreshJwtCookie(refreshToken.getToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(LogInResponse
                        .builder()
                        .accessToken(jwtCookie.getValue())
                        .roles(roles)
                        .build());
    }

    private Authentication doAuthenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            return manager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }

    }


    private String applicationUrl(HttpServletRequest request) {
        return request.getHeader("Origin");
    }
}

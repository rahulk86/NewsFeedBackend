package com.NewFeed.backend.controller;

import com.NewFeed.backend.dto.UserDto;
import com.NewFeed.backend.exception.TokenRefreshException;
import com.NewFeed.backend.modal.auth.RefreshToken;
import com.NewFeed.backend.payload.Request.Follow;
import com.NewFeed.backend.payload.Request.LoginRequest;
import com.NewFeed.backend.payload.Request.RegisterRequest;
import com.NewFeed.backend.payload.Request.SignUpRequest;
import com.NewFeed.backend.payload.Response.LogInResponse;
import com.NewFeed.backend.payload.Response.MessageResponse;
import com.NewFeed.backend.security.JwtService;
import com.NewFeed.backend.service.RefreshTokenService;
import com.NewFeed.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    JwtService jwtService;


    @PostMapping("/follow")
    public  ResponseEntity<?> follow(@Valid  @RequestBody Follow follow,Authentication authentication){
        UserDto userDto = (UserDto) authentication.getPrincipal();
        userService.follow(userDto,follow.getUserId());
        return new ResponseEntity<>(MessageResponse.
                builder().
                message("followed successfully").
                build(),
                HttpStatus.CREATED);
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
    public ResponseEntity<MessageResponse> createUser(@Valid  @RequestBody RegisterRequest registerRequest){
        try {

            UserDto userDto = userService.createUser(UserDto.builder()
                                                        .email(registerRequest.getEmail())
                                                        .password(registerRequest.getPassword())
                                                        .name(registerRequest.getUsername())
                                                        .build());

            return new ResponseEntity<>(MessageResponse.
                                        builder().
                                        message("registration completed").
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
        RefreshToken refreshToken       = refreshTokenService.createRefreshToken(userDetails);
        ResponseCookie jwtRefreshCookie = jwtService.generateRefreshJwtCookie(refreshToken.getToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(LogInResponse
                        .builder()
                        .accessToken(jwtCookie.getValue())
                        .roles(roles)
                        .build());
    }


    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser(Authentication authentication) {
        UserDto userDto = (UserDto) authentication.getPrincipal();
        refreshTokenService.deleteToken(userDto);
        ResponseCookie jwtCookie        = jwtService.getCleanJwtCookie();
        ResponseCookie jwtRefreshCookie = jwtService.getCleanJwtRefreshCookie();

        return ResponseEntity
                    .ok()
                    .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                    .body(MessageResponse.
                            builder().
                            message("You've been signed out!").
                            build());
    }

    private Authentication doAuthenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            return manager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }

    }
}

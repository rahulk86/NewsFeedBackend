package com.NewFeed.backend.controller;

import com.NewFeed.backend.dto.UserDto;
import com.NewFeed.backend.payload.Request.Follow;
import com.NewFeed.backend.payload.Response.MessageResponse;
import com.NewFeed.backend.security.JwtService;
import com.NewFeed.backend.service.RefreshTokenService;
import com.NewFeed.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usersAuth")
public class UserAuthController {
    @Autowired
    private UserService userService;
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


    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser(Authentication authentication) {
        UserDto userDto = (UserDto) authentication.getPrincipal();
        refreshTokenService.deleteToken(userDto);
        ResponseCookie jwtRefreshCookie = jwtService.getCleanJwtRefreshCookie();

        return ResponseEntity
                    .ok()
                    .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                    .body(MessageResponse.
                            builder().
                            message("You've been signed out!").
                            build());
    }

}

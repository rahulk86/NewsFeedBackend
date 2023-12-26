package com.NewFeed.backend.controller;

import com.NewFeed.backend.dto.UserDto;
import com.NewFeed.backend.dto.UserProfileDto;
import com.NewFeed.backend.exception.UserPostException;
import com.NewFeed.backend.exception.UserProfileException;
import com.NewFeed.backend.modal.image.Imageable;
import com.NewFeed.backend.payload.Response.MessageResponse;
import com.NewFeed.backend.service.ImageService;
import com.NewFeed.backend.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/users/profile")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private ImageService imageService;
    @GetMapping("/get")
    public ResponseEntity<?> getProfile(Authentication authentication ){
        try {
            UserDto principal = (UserDto) authentication.getPrincipal();
            UserProfileDto profile = userProfileService.getProfile(principal);
            return ResponseEntity.ok(profile);
        }
        catch (UserProfileException e){
            return new ResponseEntity<>(MessageResponse.
                    builder().
                    message(e.getMessage()).
                    build(),
                    HttpStatus.NOT_FOUND);
        }

    }
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllProfile(Authentication authentication ){
        try {
            UserDto principal = (UserDto) authentication.getPrincipal();
            List<UserProfileDto> profiles = userProfileService.getAllProfile(principal);
            return ResponseEntity.ok(profiles);
        }
        catch (UserProfileException e){
            return new ResponseEntity<>(MessageResponse.
                    builder().
                    message(e.getMessage()).
                    build(),
                    HttpStatus.NOT_FOUND);
        }

    }
    @PostMapping("/upload")
    public ResponseEntity<?> upload(Authentication authentication,
                                    @RequestParam("file") MultipartFile multipartFile){
        try {
            UserDto userDto = (UserDto) authentication.getPrincipal();
            Imageable imageable = userProfileService.getImageable(userDto);
            imageService.save(imageable,multipartFile);
            return new ResponseEntity<>(MessageResponse.
                    builder().
                    message("image uploaded Successfully").
                    build(), HttpStatus.CREATED);

        } catch (UserPostException e){
            return new ResponseEntity<>(MessageResponse.
                    builder().
                    message(e.getMessage()).
                    build(),
                    HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(MessageResponse.
                    builder().
                    message("IOException : "+e.getMessage()).
                    build(),
                    HttpStatus.NOT_FOUND);
        }
    }
}

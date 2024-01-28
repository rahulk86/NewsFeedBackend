package com.NewFeed.backend.controller;

import com.NewFeed.backend.dto.UserDto;
import com.NewFeed.backend.dto.UserPostDto;
import com.NewFeed.backend.exception.AWSS3ConfigException;
import com.NewFeed.backend.exception.UserPostException;
import com.NewFeed.backend.modal.feed.Votable;
import com.NewFeed.backend.modal.image.Imageable;
import com.NewFeed.backend.payload.Response.MessageResponse;
import com.NewFeed.backend.service.ImageService;
import com.NewFeed.backend.service.UserPostService;
import com.NewFeed.backend.service.VotableService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users/post")
public class UserPostController {
    private UserPostService userPostService;
    private VotableService votableService;
    private ImageService imageService;
    @PostMapping ("/create")
    public ResponseEntity<?> createPost(Authentication authentication,
                                        @RequestParam("text") String text,
                                        @RequestParam(value = "file", required = false) MultipartFile multipartFile){
        try {
            UserPostDto userPostDto = new UserPostDto();
            userPostDto.setText(text);
            UserDto principal = (UserDto) authentication.getPrincipal();
            Imageable imageable = userPostService.createPost(principal.getId(), userPostDto);

            if (multipartFile != null && !multipartFile.isEmpty()) {
                imageService.save(imageable, multipartFile);
            }

            return new ResponseEntity<>(MessageResponse.
                    builder().
                    message("Post Created Successfully").
                    build(), HttpStatus.CREATED);
        }
        catch (UserPostException | AWSS3ConfigException e){
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

    @GetMapping("/get")
    public ResponseEntity<?> getPosts(Authentication authentication ){
        try{
            UserDto principal = (UserDto) authentication.getPrincipal();
            List<UserPostDto> postByUserId = userPostService.getPosts(principal.getId());
            return ResponseEntity.ok(postByUserId);
        }
        catch (UserPostException e){
            return new ResponseEntity<>(MessageResponse.
                                            builder().
                                            message(e.getMessage()).
                                            build(),
                                        HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/vote")
    public ResponseEntity<?> upvotePost(@RequestBody UserPostDto userPostDto,Authentication authentication) {
        UserDto userDto           = (UserDto) authentication.getPrincipal();
        Object[] postBeforeVote  = userPostService.getPost(userPostDto.getId(),userDto.getId());
       if(userPostDto.isLiked()) {
            votableService.upVote(userDto.getId(), (Votable) postBeforeVote[0]);
        }
        else{
            votableService.downVote(userDto.getId(), (Votable) postBeforeVote[0]);
        }
        Object[] post       = userPostService.getPost(userPostDto.getId(),userDto.getId());
        UserPostDto postDto = userPostService.postToDto(post);
        postDto.setLiked(postDto.isLiked());
        return new ResponseEntity<>(postDto, HttpStatus.CREATED);
    }

}

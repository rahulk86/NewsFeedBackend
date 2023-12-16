package com.NewFeed.backend.controller;

import com.NewFeed.backend.dto.UserCommentDto;
import com.NewFeed.backend.dto.UserDto;
import com.NewFeed.backend.exception.UserCommentException;
import com.NewFeed.backend.exception.UserPostException;
import com.NewFeed.backend.modal.feed.Votable;
import com.NewFeed.backend.payload.Request.CommentsByPost;
import com.NewFeed.backend.payload.Request.VoteOnComments;
import com.NewFeed.backend.payload.Response.MessageResponse;
import com.NewFeed.backend.service.UserCommentService;
import com.NewFeed.backend.service.UserPostService;
import com.NewFeed.backend.service.VotableService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/post/comment")
public class UserCommentController {
    @Autowired
    private UserCommentService userCommentService;
    @Autowired
    private VotableService votableService;
    @Autowired
    private UserPostService userPostService;
    @PostMapping ("/create")
    public ResponseEntity<?> createComment(@RequestBody UserCommentDto userCommentDto, @NotNull Authentication authentication){
        try {
            UserDto principal = (UserDto) authentication.getPrincipal();
            userPostService.validateComment(userCommentDto);
            UserCommentDto comment = userCommentService.createComment(principal,userCommentDto);
            return new ResponseEntity<>(comment, HttpStatus.CREATED);
        }
        catch (UserCommentException e){
            return new ResponseEntity<>(MessageResponse.
                                            builder().
                                            message(e.getMessage()).
                                            build(),
                                        HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/get")
    public ResponseEntity<?> getComments(@RequestBody CommentsByPost commentsByPost, Authentication authentication ){
        try{
            UserDto principal = (UserDto) authentication.getPrincipal();
            List<UserCommentDto> commentByPostId = userCommentService.getCommentByPostId(principal.getId(),commentsByPost.getPostId());
            return ResponseEntity.ok(commentByPostId);
        }
        catch (UserPostException e){
            return new ResponseEntity<>(MessageResponse.
                                            builder().
                                            message(e.getMessage()).
                                            build(),
                                        HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/upVote")
    public ResponseEntity<?> upvoteComment(@RequestBody VoteOnComments voteOnComments, Authentication authentication) {
        UserDto userDto = (UserDto) authentication.getPrincipal();
        Votable votable = userCommentService.getVotable(voteOnComments);
        votableService.upVote(userDto.getId(),votable);
        UserCommentDto comment = userCommentService.votableToDto(votable);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @PostMapping("/downVote")
    public ResponseEntity<?> downVoteComment(@RequestBody VoteOnComments voteOnComments,Authentication authentication) {
        UserDto userDto = (UserDto) authentication.getPrincipal();
        Votable votable = userCommentService.getVotable(voteOnComments);
        votableService.downVote(userDto.getId(),votable);
        UserCommentDto comment = userCommentService.votableToDto(votable);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

}

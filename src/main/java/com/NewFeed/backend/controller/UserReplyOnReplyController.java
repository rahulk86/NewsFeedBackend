package com.NewFeed.backend.controller;

import com.NewFeed.backend.dto.UserReplyDto;
import com.NewFeed.backend.exception.UserCommentException;
import com.NewFeed.backend.exception.UserPostException;
import com.NewFeed.backend.modal.feed.Votable;
import com.NewFeed.backend.payload.Request.RepliesByReply;
import com.NewFeed.backend.payload.Request.VoteOnReply;
import com.NewFeed.backend.payload.Response.MessageResponse;
import com.NewFeed.backend.service.UserCommentService;
import com.NewFeed.backend.service.UserPostService;
import com.NewFeed.backend.service.UserReplyService;
import com.NewFeed.backend.service.VotableService;
import com.auth.dto.UserDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/post/comment/reply/reply")
public class UserReplyOnReplyController {
    @Autowired
    private UserCommentService userCommentService;
    @Autowired
    private UserReplyService userReplyService;
    @Autowired
    private VotableService votableService;
    @Autowired
    private UserPostService userPostService;
    @PostMapping ("/create")
    public ResponseEntity<?> createComment(@RequestBody UserReplyDto userReplyDto, @NotNull Authentication authentication){
        try {
            UserDto principal = (UserDto) authentication.getPrincipal();
            userReplyService.validateReply(userReplyDto);
            UserReplyDto reply = userReplyService.createReplyReply(principal,userReplyDto);
            return new ResponseEntity<>(reply, HttpStatus.CREATED);
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
    public ResponseEntity<?> getReplies(@RequestBody RepliesByReply repliesByReply, Authentication authentication ){
        try{
            UserDto principal = (UserDto) authentication.getPrincipal();
            List<UserReplyDto> replies = userReplyService.getReplyByReplyId(repliesByReply.getReplyId(),principal.getId());
            return ResponseEntity.ok(replies);
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
    public ResponseEntity<?> upvoteComment(@RequestBody VoteOnReply voteOnReply, Authentication authentication) {
        UserDto userDto = (UserDto) authentication.getPrincipal();
        Votable votable = userReplyService.getVotableOnReply(voteOnReply);
        votableService.upVote(userDto.getId(),votable);
        UserReplyDto reply = userReplyService.votableToDto(votable);
        return new ResponseEntity<>(reply, HttpStatus.CREATED);
    }

    @PostMapping("/downVote")
    public ResponseEntity<?> downVoteComment(@RequestBody VoteOnReply voteOnReply,Authentication authentication) {
        UserDto userDto = (UserDto) authentication.getPrincipal();
        Votable votable = userReplyService.getVotableOnReply(voteOnReply);
        votableService.downVote(userDto.getId(),votable);
        UserReplyDto reply = userReplyService.votableToDto(votable);
        return new ResponseEntity<>(reply, HttpStatus.CREATED);
    }

}

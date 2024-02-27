package com.NewFeed.backend.service;

import com.NewFeed.backend.dto.UserCommentDto;
import com.NewFeed.backend.dto.UserReplyDto;
import com.NewFeed.backend.modal.feed.Votable;
import com.NewFeed.backend.payload.Request.VoteOnComments;
import com.auth.dto.UserDto;

import java.util.List;

public interface UserCommentService {
    public UserCommentDto createComment(UserDto userDto, UserCommentDto userCommentDto);

    List<UserCommentDto> getCommentByPostId(Long userId, Long postId);

    Votable getVotable(VoteOnComments voteOnComments);
    UserCommentDto votableToDto(Votable votable);
    void validateComment(UserReplyDto userReplyDto);
}

package com.NewFeed.backend.service;

import com.NewFeed.backend.dto.UserDto;
import com.NewFeed.backend.dto.UserReplyDto;
import com.NewFeed.backend.modal.Votable;
import com.NewFeed.backend.payload.Request.VoteOnReply;

import java.util.List;

public interface UserReplyService {
    public UserReplyDto createCommentReply(UserDto userDto, UserReplyDto userReplyDto);
    public UserReplyDto createReplyReply(UserDto userDto, UserReplyDto userReplyDto);
    public List<UserReplyDto> getReplyByCommentId(Long id,Long userId);
    public List<UserReplyDto> getReplyByReplyId(Long id,Long userId);
    Votable getVotableOnReply(VoteOnReply voteOnReply);
    UserReplyDto votableToDto(Votable votable);

    Votable getVotableOnComment(VoteOnReply voteOnReply);

    void validateReply(UserReplyDto userReplyDto);
}

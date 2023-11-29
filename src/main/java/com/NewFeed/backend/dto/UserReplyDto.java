package com.NewFeed.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserReplyDto {
    private Long id;
    private String text;
    private Long user_id;
    private Long commentId;
    private Long replyId;
    private Long upVotes;
    private Long downVotes;
    private Long replies;
}

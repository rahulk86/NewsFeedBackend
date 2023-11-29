package com.NewFeed.backend.payload.Request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VoteOnReply {
    private Long commentId;
    private Long replyId;
}

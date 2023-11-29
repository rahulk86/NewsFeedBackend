package com.NewFeed.backend.payload.Request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VoteOnComments {
    private Long postId;
    private Long commentId;
}

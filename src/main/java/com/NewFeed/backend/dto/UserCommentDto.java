package com.NewFeed.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCommentDto {
    private Long id;
    private String text;
    private boolean liked;
    private boolean disLiked;
    private UserProfileDto userProfile;
    private Long postId;
    private Long upVotes;
    private Long downVotes;
    private Long replies;
}

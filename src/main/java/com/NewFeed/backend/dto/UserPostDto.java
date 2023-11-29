package com.NewFeed.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserPostDto {
    private Long id;
    private boolean liked;
    private boolean disLiked;
    private ImageDto image;
    private String text;
    private UserProfileDto userProfile;
    private Long upVote;
    private Long downVote;
    private Long comments;
}

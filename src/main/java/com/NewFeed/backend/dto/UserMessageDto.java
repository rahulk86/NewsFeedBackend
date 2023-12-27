package com.NewFeed.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserMessageDto {
    private Long id ;
    private LocalDateTime creatAt;
    private Long messengerId;
    private String text;
    private ImageDto image;
}

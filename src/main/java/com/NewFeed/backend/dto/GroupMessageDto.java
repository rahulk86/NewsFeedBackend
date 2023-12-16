package com.NewFeed.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GroupMessageDto {
    private Long id ;
    private MessengerDto messenger;
    private LocalDateTime creatAt;
    private String text;
    private ImageDto image;
}

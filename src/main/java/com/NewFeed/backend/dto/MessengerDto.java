package com.NewFeed.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessengerDto {
    private Long id;
    private String name;
    private ImageDto image;
    private Long conversationId;
    private String type;
    private LocalDateTime creatAt;
    private Long unreadMessages;
}

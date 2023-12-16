package com.NewFeed.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessengerDto {
    private String name;
    private ImageDto image;
    private Long id;
    private String type;
    private LocalDateTime lastUpdate;
}

package com.NewFeed.backend.dto;

import com.NewFeed.backend.modal.messaging.GroupRole;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GroupMemberDto {
    private Long id;
    private String name;
    private ImageDto image;
    private GroupRole role;
    private LocalDateTime lastUpdate;
}

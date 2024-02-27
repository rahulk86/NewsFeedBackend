package com.NewFeed.backend.dto;

import com.auth.dto.UserDto;
import com.auth.modal.user.GenderType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserProfileDto {
    private UserDto user;
    private ImageDto image;
    private String address;
    private LocalDateTime date;
    private int phoneNumber;
    private GenderType gender;
}

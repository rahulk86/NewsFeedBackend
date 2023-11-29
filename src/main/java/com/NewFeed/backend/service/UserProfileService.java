package com.NewFeed.backend.service;

import com.NewFeed.backend.dto.UserDto;
import com.NewFeed.backend.dto.UserProfileDto;
import com.NewFeed.backend.modal.UserProfile;

public interface UserProfileService {
    UserProfile updateProfile(UserProfileDto userProfileDto);

    UserProfile getImageable(UserDto userDto);

    UserProfileDto getProfile(UserDto userDto);
}

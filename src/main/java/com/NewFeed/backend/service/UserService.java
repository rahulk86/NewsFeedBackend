package com.NewFeed.backend.service;


import com.NewFeed.backend.dto.UserDto;
import com.NewFeed.backend.modal.user.NewFeedUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService  extends UserDetailsService {
     UserDto createUser(UserDto userDto);
     void signup(UserDto userDto);
     UserDto getUserById(Long id);
     NewFeedUser toNewFeedUser(UserDto userDto);
     void follow(UserDto userDto,Long followedUserId);
}

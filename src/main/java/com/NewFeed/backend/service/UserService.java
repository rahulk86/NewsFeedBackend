package com.NewFeed.backend.service;


import com.NewFeed.backend.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService  extends UserDetailsService {
     UserDto createUser(UserDto userDto);
     void signup(UserDto userDto);
     UserDto getUserById(Long id);
     void follow(UserDto userDto,Long followedUserId);
}

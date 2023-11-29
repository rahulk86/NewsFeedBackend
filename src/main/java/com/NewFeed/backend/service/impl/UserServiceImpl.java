package com.NewFeed.backend.service.impl;


import com.NewFeed.backend.dto.UserDto;
import com.NewFeed.backend.modal.*;
import com.NewFeed.backend.repository.FollowedRepository;
import com.NewFeed.backend.repository.RoleRepository;
import com.NewFeed.backend.repository.UserProfileRepository;
import com.NewFeed.backend.repository.UserRepository;
import com.NewFeed.backend.security.JwtService;
import com.NewFeed.backend.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FollowedRepository followedRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private JwtService tokensService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    @Qualifier("userServiceModelMapper")
    private ModelMapper modelMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        if (this.userRepository.existsByEmail(userDto.getEmail())) {
            throw new DataIntegrityViolationException("User already exists with given email : " + userDto.getEmail());
        }
        Role role = roleRepository.findByName(ERole.ROLE_ADMIN).orElse(null);
        if (role == null){
            role = new Role();
            role.setName(ERole.ROLE_ADMIN);
            role.setCreatAt(LocalDateTime.now());
            role.setActive(1);
            role = roleRepository.save(role);
        }

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        NewFeedUser newFeedUser = this.modelMapper.map(userDto, NewFeedUser.class);
        newFeedUser.setRoles(roles);
        NewFeedUser user = this.userRepository.save(newFeedUser);

        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        userProfile.setActive(1);
        userProfile.setCreatAt(LocalDateTime.now());
        userProfileRepository.save(userProfile);

        return  this.modelMapper.map(this.userRepository.save(newFeedUser), UserDto.class);
    }

    @Override
    public void signup(UserDto userDto) {
        if (this.userRepository.existsByEmail(userDto.getEmail())) {
            throw new DataIntegrityViolationException("User already exists with given email : " + userDto.getEmail());
        }
    }

    @Override
    public UserDto getUserById(Long id) {
        NewFeedUser newFeedUser = this.userRepository.
                                     findById(id).
                                     orElseThrow(() -> new DataIntegrityViolationException("User is not exists with given id : " + id));
        return this.modelMapper.map(newFeedUser, UserDto.class);
    }

    @Override
    public void follow(UserDto userDto, Long followedUserId) {
        NewFeedUser user = this.userRepository.
                findById(userDto.getId()).
                orElseThrow(() -> new DataIntegrityViolationException("User is not exists with given id : " + userDto.getId()));
        NewFeedUser followedUser = this.userRepository.
                findById(followedUserId).
                orElseThrow(() -> new DataIntegrityViolationException("User is not exists with given id : " +followedUserId));

        Followed followed1 = followedRepository.findByUserAndFollowedUser(user, followedUser).orElse(null);
        if(followed1==null) {
            Followed followed = new Followed();
            followed.setActive(1);
            followed.setCreatAt(LocalDateTime.now());
            followed.setUser(user);
            followed.setFollowedUser(followedUser);
            followedRepository.save(followed);
        }
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        NewFeedUser newFeedUser = this.userRepository.
                findByEmail(email).
                orElseThrow(() -> new DataIntegrityViolationException("User is not exists with given email : " + email));
        return this.modelMapper.map(newFeedUser, UserDto.class);
    }
}

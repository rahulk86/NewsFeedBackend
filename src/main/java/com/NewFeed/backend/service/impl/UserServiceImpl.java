package com.NewFeed.backend.service.impl;


import com.NewFeed.backend.configuration.security.AppProperties;
import com.NewFeed.backend.dto.UserDto;
import com.NewFeed.backend.modal.feed.Followed;
import com.NewFeed.backend.modal.user.ERole;
import com.NewFeed.backend.modal.user.NewFeedUser;
import com.NewFeed.backend.modal.user.Role;
import com.NewFeed.backend.repository.feed.FollowedRepository;
import com.NewFeed.backend.repository.user.RoleRepository;
import com.NewFeed.backend.repository.user.UserRepository;
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

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private AppProperties appProperties;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowedRepository followedRepository;

    @Autowired
    private JwtService tokensService;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    @Qualifier("userServiceModelMapper")
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        NewFeedUser newFeedUser1 = userRepository
                                    .findByEmail(userDto.getEmail())
                                    .orElse(null);
        if(newFeedUser1!=null && newFeedUser1.getActive()==0){
            return this.modelMapper.map(newFeedUser1, UserDto.class);
        }else if(newFeedUser1!=null && newFeedUser1.getActive()==1){
            throw new DataIntegrityViolationException("User already exists with given email : " + userDto.getEmail());
        }

        Role role = roleRepository.findByName(ERole.ROLE_ADMIN)
                        .orElseGet(()->{Role newRole = new Role();
                            newRole.setName(ERole.ROLE_ADMIN);
                            newRole.setCreatAt(appProperties.now());
                            newRole.setActive(1);
                            return roleRepository.save(newRole);
                        });

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        NewFeedUser newFeedUser = this.modelMapper.map(userDto, NewFeedUser.class);
        newFeedUser.setRoles(roles);
        newFeedUser.setCreatAt(appProperties.now());
        newFeedUser.setActive(0);
        NewFeedUser user = this.userRepository.save(newFeedUser);

        return  this.modelMapper.map(user, UserDto.class);
    }

    @Override
    @Transactional
    public void signup(UserDto userDto) {
        NewFeedUser user = userRepository
                                .findByEmail(userDto.getEmail())
                                .orElse(null);
        if(user!=null && user.getActive()==1){
            throw new DataIntegrityViolationException("User already exists with given email : " + userDto.getEmail());
        }
    }

    @Override
    @Transactional
    public UserDto getUserById(Long id) {
        NewFeedUser newFeedUser = this.userRepository.
                                     findById(id).
                                     orElseThrow(() -> new DataIntegrityViolationException("User is not exists with given id : " + id));
        return this.modelMapper.map(newFeedUser, UserDto.class);
    }

    @Override
    public NewFeedUser toNewFeedUser(UserDto userDto) {
        return this.userRepository.
                    findById(userDto.getId()).
                    orElseThrow(() -> new DataIntegrityViolationException("User is not exists with given id : " + userDto.getId()));

    }

    @Override
    @Transactional
    public void follow(UserDto userDto, Long followedUserId) {
        NewFeedUser user = toNewFeedUser(userDto);
        NewFeedUser followedUser = this.userRepository.
                findById(followedUserId).
                orElseThrow(() -> new DataIntegrityViolationException("User is not exists with given id : " +followedUserId));

        Followed followed1 = followedRepository.findByUserAndFollowedUser(user, followedUser).orElse(null);
        if(followed1==null) {
            Followed followed = new Followed();
            followed.setActive(1);
            followed.setCreatAt(appProperties.now());
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
                                        orElse(null);
        if(newFeedUser==null || newFeedUser.getActive()==0){
          throw new DataIntegrityViolationException("User is not exists with given email : " + email);
        }

        return this.modelMapper.map(newFeedUser, UserDto.class);
    }
}

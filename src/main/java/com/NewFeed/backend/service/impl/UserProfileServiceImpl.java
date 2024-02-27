package com.NewFeed.backend.service.impl;

import com.NewFeed.backend.dto.ImageDto;
import com.NewFeed.backend.dto.UserProfileDto;
import com.NewFeed.backend.exception.UserProfileException;
import com.NewFeed.backend.modal.image.Image;
import com.NewFeed.backend.modal.user.UserProfile;
import com.NewFeed.backend.repository.user.UserProfileRepository;
import com.NewFeed.backend.service.UserProfileService;
import com.auth.dto.UserDto;
import com.auth.modal.user.User;
import com.auth.repository.user.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Qualifier("userImageServiceModelMapper")
    private ModelMapper imageModelMapper;

    @Autowired
    @Qualifier("userServiceModelMapper")
    private ModelMapper userModelMapper;

    @Override
    @Transactional
    public UserProfile getUserProfile(UserDto userDto){
        User user = userRepository
                .findByEmail(userDto.getEmail())
                .orElseThrow(()-> new UserProfileException("UserProfileException!! User is not exists with given id :" + userDto.getEmail()));

        return userProfileRepository
                .findByUser(user)
                .orElseGet(()->creatUserProfile(user));

    }
    private UserProfile creatUserProfile(User user){
        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
       return userProfileRepository.save(userProfile);
    }
    @Override
    @Transactional
    public UserProfile updateProfile(UserProfileDto userProfileDto) {
        UserProfile userProfile = getUserProfile(userProfileDto.getUser());
        userProfile.setAddress(userProfileDto.getAddress());
        userProfile.setGender(userProfileDto.getGender());
        userProfile.setPhoneNumber(userProfileDto.getPhoneNumber());

        return userProfileRepository.save(userProfile);
    }

    @Override
    @Transactional
    public UserProfile getImageable(UserDto userDto) {
        User user = userRepository
                .findByEmail(userDto.getEmail())
                .orElseThrow(()-> new UserProfileException("UserProfileException!! User is not exists with given id :" + userDto.getEmail()));
        Object[] profileDetails = userProfileRepository
                .findByUserWithImage(user)
                .orElseThrow(()-> new UserProfileException("UserProfileException!! profile not exit for email id :" + userDto.getEmail()));
        Object[] profile = (Object[]) profileDetails[0];
        return  (UserProfile) profile[0];
    }

    private UserProfileDto toUserProfileDto(Object[] profile){
        UserProfile userProfile = (UserProfile) profile[0];
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setAddress(userProfile.getAddress());
        userProfileDto.setGender(userProfile.getGender());
        userProfileDto.setPhoneNumber(userProfile.getPhoneNumber());
        userProfileDto.setUser(userModelMapper.map(userProfile.getUser(),UserDto.class));

        Image image             = profile.length>1?(Image) profile[1]:null;
        if(image!=null){
            userProfileDto.setImage(imageModelMapper.map(image, ImageDto.class));
        }
        return userProfileDto;
    }
    @Override
    @Transactional
    public UserProfileDto getProfile(UserDto userDto) {
        User user = userRepository
                .findByEmail(userDto.getEmail())
                .orElseThrow(()-> new UserProfileException("UserProfileException!! User is not exists with given id :" + userDto.getEmail()));

        Object[] profileDetails = userProfileRepository
                .findByUserWithImage(user)
                .orElseThrow(()-> new UserProfileException("UserProfileException!! profile not exit for email id :" + userDto.getEmail()));
        Object[] profile = (Object[]) profileDetails[0];
        return toUserProfileDto(profile);
    }

    @Override
    @Transactional
    public List<UserProfileDto> getAllProfile(UserDto userDto) {
        User user = userRepository
                .findByEmail(userDto.getEmail())
                .orElseThrow(()-> new UserProfileException("UserProfileException!! User is not exists with given id :" + userDto.getEmail()));
        return userProfileRepository
                .findAllByUserWithImage()
                .stream()
                .map(this::toUserProfileDto)
                .toList();
    }
}

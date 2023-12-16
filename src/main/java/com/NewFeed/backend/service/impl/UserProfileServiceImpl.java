package com.NewFeed.backend.service.impl;

import com.NewFeed.backend.dto.ImageDto;
import com.NewFeed.backend.dto.UserDto;
import com.NewFeed.backend.dto.UserProfileDto;
import com.NewFeed.backend.exception.UserProfileException;
import com.NewFeed.backend.modal.image.Image;
import com.NewFeed.backend.modal.user.NewFeedUser;
import com.NewFeed.backend.modal.user.UserProfile;
import com.NewFeed.backend.repository.user.UserProfileRepository;
import com.NewFeed.backend.repository.user.UserRepository;
import com.NewFeed.backend.service.UserProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class UserProfileServiceImpl implements UserProfileService {
    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Qualifier("userImageServiceModelMapper")
    private ModelMapper imageModelMapper;

    @Override
    public UserProfile getUserProfile(UserDto userDto){
        NewFeedUser user = userRepository
                .findByEmail(userDto.getEmail())
                .orElseThrow(()-> new UserProfileException("UserProfileException!! User is not exists with given id :" + userDto.getEmail()));

        return userProfileRepository
                .findByUser(user)
                .orElseThrow(()-> new UserProfileException("UserProfileException!! profile not exit for email id :" + userDto.getEmail()));

    }
    @Override
    public UserProfile updateProfile(UserProfileDto userProfileDto) {
        UserProfile userProfile = getUserProfile(userProfileDto.getUser());
        userProfile.setAddress(userProfileDto.getAddress());
        userProfile.setGender(userProfileDto.getGender());
        userProfile.setPhoneNumber(userProfileDto.getPhoneNumber());

        return userProfileRepository.save(userProfile);
    }

    @Override
    public UserProfile getImageable(UserDto userDto) {
        NewFeedUser user = userRepository
                .findByEmail(userDto.getEmail())
                .orElseThrow(()-> new UserProfileException("UserProfileException!! User is not exists with given id :" + userDto.getEmail()));
        Object[] profileDetails = userProfileRepository
                .findByUserWithImage(user)
                .orElseThrow(()-> new UserProfileException("UserProfileException!! profile not exit for email id :" + userDto.getEmail()));
        Object[] profile = (Object[]) profileDetails[0];
        return  (UserProfile) profile[0];
    }
    @Override
    public UserProfileDto getProfile(UserDto userDto) {
        NewFeedUser user = userRepository
                .findByEmail(userDto.getEmail())
                .orElseThrow(()-> new UserProfileException("UserProfileException!! User is not exists with given id :" + userDto.getEmail()));

        Object[] profileDetails = userProfileRepository
                .findByUserWithImage(user)
                .orElseThrow(()-> new UserProfileException("UserProfileException!! profile not exit for email id :" + userDto.getEmail()));
        Object[] profile = (Object[]) profileDetails[0];
        UserProfile userProfile = (UserProfile) profile[0];
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setUser(userDto);
        userProfileDto.setAddress(userProfile.getAddress());
        userProfileDto.setGender(userProfile.getGender());
        userProfileDto.setPhoneNumber(userProfile.getPhoneNumber());
        userProfileDto.setDate(userProfile.getCreatAt());

        Image image             = profile.length>1?(Image) profile[1]:null;
        if(image!=null){
            userProfileDto.setImage(imageModelMapper.map(image, ImageDto.class));
        }

        return userProfileDto;
    }
}

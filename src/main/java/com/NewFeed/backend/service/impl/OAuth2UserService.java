package com.NewFeed.backend.service.impl;

import com.NewFeed.backend.modal.image.Image;
import com.NewFeed.backend.modal.user.UserProfile;
import com.NewFeed.backend.repository.image.ImageRepository;
import com.NewFeed.backend.repository.user.UserProfileRepository;
import com.auth.modal.user.User;
import com.auth.service.impl.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OAuth2UserService extends CustomOAuth2UserService {
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Override
    public void processOAuth2User(User user,String imageUrl) {
        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);

        userProfile = userProfileRepository.save(userProfile);

        Image newImage = new Image();
        newImage.setUrl(imageUrl);
        newImage.setImageableId(userProfile.getId());
        newImage.setImageableType(userProfile.getClass().getSimpleName());
        imageRepository.save(newImage);
    }
}
package com.NewFeed.backend.security.oauth2;


import com.NewFeed.backend.dto.UserDto;
import com.NewFeed.backend.exception.OAuth2AuthenticationProcessingException;
import com.NewFeed.backend.modal.*;
import com.NewFeed.backend.repository.*;
import com.NewFeed.backend.security.oauth2.user.OAuth2UserInfo;
import com.NewFeed.backend.security.oauth2.user.OAuth2UserInfoFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private AuthProviderRepository authProviderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;


    @Autowired
    @Qualifier("userServiceModelMapper")
    private ModelMapper modelMapper;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if(oAuth2UserInfo.getEmail().isBlank()) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        NewFeedUser newFeedUser  = userRepository
                                        .findByEmail(oAuth2UserInfo.getEmail())
                                        .orElseGet(() -> createUser(oAuth2UserInfo));

        AuthProvider authProvider = authProviderRepository
                                        .findByUser(newFeedUser)
                                        .orElseGet(() ->registerNewUser(
                                                                oAuth2UserRequest,
                                                                oAuth2UserInfo ,
                                                                newFeedUser
                                                               )
                                        );

        authProviderRepository.save(authProvider);
        UserDto userDto = modelMapper.map(newFeedUser, UserDto.class);
        userDto.setAttributes(oAuth2User.getAttributes());
        return userDto;
    }

        private AuthProvider registerNewUser(OAuth2UserRequest oAuth2UserRequest,
                                             OAuth2UserInfo oAuth2UserInfo,
                                             NewFeedUser user) {

        AuthProvider authProvider = new AuthProvider();
        authProvider.setActive(1);
        authProvider.setCreatAt(LocalDateTime.now());
        authProvider.setName(AuthProviderType.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        authProvider.setProviderId(oAuth2UserInfo.getId());
        authProvider.setUser(user);

        return authProvider;
    }

    private NewFeedUser createUser(OAuth2UserInfo oAuth2UserInfo){
        Role role = roleRepository
                        .findByName(ERole.ROLE_ADMIN)
                        .orElse(null);

        if (role == null){
            role = new Role();
            role.setName(ERole.ROLE_ADMIN);
            role.setCreatAt(LocalDateTime.now());
            role.setActive(1);
            role = roleRepository.save(role);
        }

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        NewFeedUser newFeedUser = new NewFeedUser();
        newFeedUser.setId(null);
        newFeedUser.setActive(1);
        newFeedUser.setCreatAt(LocalDateTime.now());
        newFeedUser.setName(oAuth2UserInfo.getName());
        newFeedUser.setEmail(oAuth2UserInfo.getEmail());
        newFeedUser.setPassword(oAuth2UserInfo.getEmail());
        newFeedUser.setRoles(roles);

        NewFeedUser user        = this.userRepository.save(newFeedUser);
        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        userProfile.setActive(1);
        userProfile.setCreatAt(LocalDateTime.now());

        userProfile = userProfileRepository.save(userProfile);

        Image newImage = new Image();
        newImage.setName(oAuth2UserInfo.getImageUrl());
        newImage.setContentType("url");
        newImage.setImageableId(userProfile.getId());
        newImage.setImageableType(userProfile.getClass().getSimpleName());
        newImage.setActive(1);
        newImage.setCreateAt(LocalDateTime.now());
        imageRepository.save(newImage);

       return this.userRepository.save(newFeedUser);
    }

}

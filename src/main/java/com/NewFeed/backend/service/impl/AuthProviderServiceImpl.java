package com.NewFeed.backend.service.impl;

import com.NewFeed.backend.exception.AuthProviderException;
import com.NewFeed.backend.modal.auth.AuthProvider;
import com.NewFeed.backend.modal.user.NewFeedUser;
import com.NewFeed.backend.repository.auth.AuthProviderRepository;
import com.NewFeed.backend.repository.user.RoleRepository;
import com.NewFeed.backend.repository.user.UserRepository;
import com.NewFeed.backend.security.oauth2.user.OAuth2UserInfo;
import com.NewFeed.backend.service.AuthProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;

@Service
public class AuthProviderServiceImpl implements AuthProviderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthProviderRepository authProviderRepository;
    @Override
    public AuthProvider getAuthProvider(String email) {

        NewFeedUser user =  userRepository
                            .findByEmail(email)
                            . orElseThrow(() -> new AuthProviderException("AuthProviderException !! User is not exists with given email :" + email));
        return authProviderRepository
                .findByUser(user)
                . orElse(null);

    }

    @Override
    public AuthProvider registerAuthProvider(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        return null;
    }
}

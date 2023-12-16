package com.NewFeed.backend.service;

import com.NewFeed.backend.modal.auth.AuthProvider;
import com.NewFeed.backend.security.oauth2.user.OAuth2UserInfo;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

public interface AuthProviderService {
    AuthProvider getAuthProvider(String email);
    AuthProvider registerAuthProvider(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo);

}

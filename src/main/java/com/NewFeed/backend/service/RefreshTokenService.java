package com.NewFeed.backend.service;

import com.NewFeed.backend.dto.UserDto;
import com.NewFeed.backend.modal.auth.RefreshToken;
import com.NewFeed.backend.modal.user.NewFeedUser;

public interface RefreshTokenService {
    RefreshToken getToken(String token);
    RefreshToken createRefreshToken(NewFeedUser user);
    RefreshToken verifyExpiration(RefreshToken token);
    UserDto getUser(String refreshToken);
    void deleteToken(NewFeedUser user);
}

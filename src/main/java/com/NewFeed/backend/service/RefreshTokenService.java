package com.NewFeed.backend.service;

import com.NewFeed.backend.dto.UserDto;
import com.NewFeed.backend.modal.auth.RefreshToken;

public interface RefreshTokenService {
    RefreshToken getToken(String token);
    RefreshToken createRefreshToken(UserDto userDto);
    RefreshToken verifyExpiration(RefreshToken token);
    UserDto getUser(String refreshToken);
    int deleteToken(UserDto userDto);
}

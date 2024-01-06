package com.NewFeed.backend.service.impl;

import com.NewFeed.backend.configuration.security.AppProperties;
import com.NewFeed.backend.dto.UserDto;
import com.NewFeed.backend.exception.TokenRefreshException;
import com.NewFeed.backend.modal.user.NewFeedUser;
import com.NewFeed.backend.modal.auth.RefreshToken;
import com.NewFeed.backend.repository.auth.RefreshTokenRepository;
import com.NewFeed.backend.repository.user.UserRepository;
import com.NewFeed.backend.service.RefreshTokenService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

  @Autowired
  private AppProperties appProperties;

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;
  @Autowired
  @Qualifier("userServiceModelMapper")
  private ModelMapper modelMapper;
  @Autowired
  private UserRepository userRepository;


  @Override
  @Transactional
  public RefreshToken getToken(String token) {
    return refreshTokenRepository
                .findByToken(token)
                .orElseThrow(()->new TokenRefreshException(token,"TokenRefreshException : Refresh token is not in database!"));
  }

  @Override
  @Transactional
  public RefreshToken createRefreshToken(UserDto userDto) {
    NewFeedUser newFeedUser = userRepository
            .findByEmail(userDto.getEmail())
            .orElseThrow(()->new TokenRefreshException("TokenRefreshException !! User is not exists with given email :" + userDto.getEmail()));
    deleteToken(newFeedUser);
    RefreshToken refreshToken = new RefreshToken();
    LocalDateTime currentTime = appProperties.now();
    refreshToken.setUser(newFeedUser);
    refreshToken.setExpiryDate(currentTime.plus(appProperties.getAuth().getJwtRefreshExpirationMs(), ChronoUnit.MILLIS));
    refreshToken.setToken(UUID.randomUUID().toString());
    refreshToken.setCreatAt(currentTime);
    refreshToken.setActive(1);

    return refreshTokenRepository.save(refreshToken);
  }

  @Override
  @Transactional
  public RefreshToken verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(appProperties.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new TokenRefreshException(token.getToken(),"TokenRefreshException : Refresh token was expired. Please make a new signin request");
    }

    return token;
  }

  @Override
  @Transactional
  public UserDto getUser(String refreshToken) {
    if (refreshToken == null || refreshToken.length() <= 0){
      throw new TokenRefreshException("TokenRefreshException : Invalid refreshToken!");
    }
    return refreshTokenRepository
            .findByToken(refreshToken)
            .map(this::verifyExpiration)
            .map(token->this.modelMapper.map(token.getUser(), UserDto.class))
            .orElseThrow(()->new TokenRefreshException("TokenRefreshException : Refresh token is not in database!"));
  }

  @Transactional
  public void deleteToken(NewFeedUser newFeedUser) {
    refreshTokenRepository
            .findByUser(newFeedUser)
            .ifPresent(byUser -> refreshTokenRepository.delete(byUser));

  }
  @Transactional
  @Override
  public void deleteToken(UserDto userDto) {
    NewFeedUser newFeedUser = userRepository
            .findByEmail(userDto.getEmail())
            .orElseThrow(()->new TokenRefreshException("TokenRefreshException !! User is not exists with given email :" + userDto.getEmail()));
    deleteToken(newFeedUser);

  }
}

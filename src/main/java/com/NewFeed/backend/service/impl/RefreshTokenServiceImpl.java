package com.NewFeed.backend.service.impl;

import com.NewFeed.backend.configuration.AppProperties;
import com.NewFeed.backend.dto.UserDto;
import com.NewFeed.backend.exception.TokenRefreshException;
import com.NewFeed.backend.modal.user.NewFeedUser;
import com.NewFeed.backend.modal.auth.RefreshToken;
import com.NewFeed.backend.repository.auth.RefreshTokenRepository;
import com.NewFeed.backend.repository.user.UserRepository;
import com.NewFeed.backend.service.RefreshTokenService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
  public RefreshToken getToken(String token) {
    return refreshTokenRepository
                .findByToken(token)
                .orElseThrow(()->new TokenRefreshException(token,"TokenRefreshException : Refresh token is not in database!"));
  }

  @Override
  public RefreshToken createRefreshToken(UserDto userDto) {
    NewFeedUser newFeedUser = userRepository
            .findByEmail(userDto.getEmail())
            .orElseThrow(()->new TokenRefreshException("TokenRefreshException !! User is not exists with given email :" + userDto.getEmail()));

    RefreshToken byUser = refreshTokenRepository.findByUser(newFeedUser).orElse(null);
    if(byUser!=null){
      refreshTokenRepository.delete(byUser);
    }
    RefreshToken refreshToken = new RefreshToken();
    LocalDateTime currentTime = LocalDateTime.now();
    refreshToken.setUser(newFeedUser);
    refreshToken.setExpiryDate(currentTime.now().plus(appProperties.getAuth().getJwtRefreshExpirationMs(), ChronoUnit.MILLIS));
    refreshToken.setToken(UUID.randomUUID().toString());
    refreshToken.setCreatAt(currentTime);
    refreshToken.setActive(1);

    return refreshTokenRepository.save(refreshToken);
  }

  @Override
  public RefreshToken verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(LocalDateTime.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new TokenRefreshException(token.getToken(),"TokenRefreshException : Refresh token was expired. Please make a new signin request");
    }

    return token;
  }

  @Override
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
  @Override
  public int deleteToken(UserDto userDto) {
    return refreshTokenRepository
            .deleteByUser(userRepository.findById(userDto.getId())
            .get());
  }
}

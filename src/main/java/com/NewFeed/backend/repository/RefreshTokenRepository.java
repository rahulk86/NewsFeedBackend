package com.NewFeed.backend.repository;

import com.NewFeed.backend.modal.NewFeedUser;
import com.NewFeed.backend.modal.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String token);
  Optional<RefreshToken> findByUser(NewFeedUser user);
  @Modifying
  int deleteByUser(NewFeedUser user);
}

package com.NewFeed.backend.modal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "refreshtoken")
public class RefreshToken extends BaseModel{
  @OneToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private NewFeedUser user;

  @Column(nullable = false, unique = true)
  private String token;

  @Column(nullable = false)
  private LocalDateTime expiryDate;
}

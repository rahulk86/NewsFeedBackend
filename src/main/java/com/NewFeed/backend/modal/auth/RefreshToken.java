package com.NewFeed.backend.modal.auth;

import com.NewFeed.backend.modal.BaseModel;
import com.NewFeed.backend.modal.user.NewFeedUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "refreshtoken")
public class RefreshToken extends BaseModel {
  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private NewFeedUser user;

  @Column(nullable = false, unique = true)
  private String token;

  @Column(nullable = false)
  private LocalDateTime expiryDate;
}

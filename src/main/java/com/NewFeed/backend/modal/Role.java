package com.NewFeed.backend.modal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "roles")
public class Role extends BaseModel{
  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private ERole name;

}
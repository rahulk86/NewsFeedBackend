package com.NewFeed.backend.modal;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public class BaseModel {
    protected int active;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    protected LocalDateTime creatAt;
}

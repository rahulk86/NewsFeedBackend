package com.NewFeed.backend.modal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public class NewFeedTextContent {
    protected int active;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    protected LocalDateTime creatAt;
    protected String text;
}

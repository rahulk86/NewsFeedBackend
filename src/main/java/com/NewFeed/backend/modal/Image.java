package com.NewFeed.backend.modal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Image{
    protected int active;
    @Id
    @GeneratedValue(generator = "uuid2")
    private UUID  id ;
    protected LocalDateTime createAt;
    private String contentType;
    private String imageableType;
    private Long imageableId;
    private String name;
}

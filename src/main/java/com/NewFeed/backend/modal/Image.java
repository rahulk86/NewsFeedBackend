package com.NewFeed.backend.modal;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Image extends BaseModel{
//    private String contentType;
    private String imageableType;
    private Long imageableId;
    private String url;
}

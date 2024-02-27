package com.NewFeed.backend.modal.image;

import com.auth.modal.BaseModel;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Image extends BaseModel {
    private String imageableType;
    private Long imageableId;
    private String url;
}

package com.NewFeed.backend.modal;

import com.auth.modal.BaseModel;
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
public class NewFeedTextContent extends BaseModel {
    protected String text;
}

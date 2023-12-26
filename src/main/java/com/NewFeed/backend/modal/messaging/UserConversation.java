package com.NewFeed.backend.modal.messaging;

import com.NewFeed.backend.modal.BaseModel;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class UserConversation extends BaseModel {

    public UserConversation(){
        this.active = 1;
        this.creatAt = LocalDateTime.now();
    }
}

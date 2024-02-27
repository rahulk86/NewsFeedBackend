package com.NewFeed.backend.modal.messaging;

import com.auth.modal.BaseModel;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class GroupMessenger extends BaseModel {
    private String name;
    @OneToOne
    private GroupConversation conversation;
}

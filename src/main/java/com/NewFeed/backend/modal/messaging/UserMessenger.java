package com.NewFeed.backend.modal.messaging;

import com.NewFeed.backend.modal.BaseModel;
import com.NewFeed.backend.modal.user.UserProfile;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UserMessenger extends BaseModel{
    @ManyToOne
    private UserConversation conversation;
    @ManyToOne
    private UserProfile profile;

}

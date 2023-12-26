package com.NewFeed.backend.modal.messaging;

import com.NewFeed.backend.modal.BaseModel;
import com.NewFeed.backend.modal.user.UserProfile;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UserMessenger extends BaseModel implements MessengerType{
    @OneToOne
    private UserProfile profile;
    @OneToOne
    private UserConversation conversation;

}

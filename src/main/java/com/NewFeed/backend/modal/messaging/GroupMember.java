package com.NewFeed.backend.modal.messaging;

import com.NewFeed.backend.modal.user.UserProfile;
import com.auth.modal.BaseModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class GroupMember extends BaseModel {
    @ManyToOne
    private GroupConversation conversation;
    @ManyToOne
    private UserProfile profile;
    @Enumerated(EnumType.STRING)
    private GroupRole role;
}

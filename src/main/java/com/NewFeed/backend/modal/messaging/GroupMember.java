package com.NewFeed.backend.modal.messaging;

import com.NewFeed.backend.modal.BaseModel;
import com.NewFeed.backend.modal.user.UserProfile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class GroupMember extends BaseModel {
    @ManyToOne
    private GroupMessenger groupMessenger;
    @OneToOne
    private UserProfile profile;
    @Enumerated(EnumType.STRING)
    private GroupRole role;
}

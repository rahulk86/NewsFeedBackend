package com.NewFeed.backend.modal.messaging;

import com.NewFeed.backend.modal.BaseModel;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class GroupMessenger extends BaseModel implements MessengerType{
    private String name;
    @OneToOne
    private Messenger messenger;
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "groupMessenger")
    private List<GroupMember> groupMembers;
}

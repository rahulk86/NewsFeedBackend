package com.NewFeed.backend.modal.messaging;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class GroupConversation extends Conversation {
    @OneToMany(mappedBy = "conversation")
    private List<GroupMember> members;
}

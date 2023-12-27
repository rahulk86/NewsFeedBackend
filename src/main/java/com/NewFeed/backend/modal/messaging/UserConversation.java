package com.NewFeed.backend.modal.messaging;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class UserConversation extends Conversation {
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "conversation")
    private List<UserMessenger> member;
}

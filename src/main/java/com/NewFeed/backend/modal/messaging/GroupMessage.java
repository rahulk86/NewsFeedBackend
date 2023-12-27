package com.NewFeed.backend.modal.messaging;

import com.NewFeed.backend.modal.NewFeedTextContent;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class GroupMessage extends NewFeedTextContent {
    @ManyToOne
    private GroupMember groupMember;
}

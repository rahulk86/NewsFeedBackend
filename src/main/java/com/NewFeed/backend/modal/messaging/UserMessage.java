package com.NewFeed.backend.modal.messaging;

import com.NewFeed.backend.modal.NewFeedTextContent;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UserMessage extends NewFeedTextContent {
    @ManyToOne
    private UserMessenger messenger;
}

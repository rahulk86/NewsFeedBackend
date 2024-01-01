package com.NewFeed.backend.modal.messaging;

import com.NewFeed.backend.modal.BaseModel;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UnreadUserMessage extends BaseModel {
    @OneToOne
    private UserMessage message;
    @ManyToOne
    private UserMessenger messenger;
}

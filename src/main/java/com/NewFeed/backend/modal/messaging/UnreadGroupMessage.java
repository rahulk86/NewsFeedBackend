package com.NewFeed.backend.modal.messaging;

import com.NewFeed.backend.modal.BaseModel;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UnreadGroupMessage extends BaseModel {
    @ManyToOne
    private GroupMessage message;
    @ManyToOne
    private GroupMember member;
}

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
public class UnreadGroupMessage extends BaseModel {
    @OneToOne
    private GroupMessage message;
    @ManyToOne
    private GroupMember member;
}

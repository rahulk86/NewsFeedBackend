package com.NewFeed.backend.modal.feed;

import com.NewFeed.backend.modal.BaseModel;
import com.NewFeed.backend.modal.user.NewFeedUser;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Vote extends BaseModel {
    @ManyToOne
    private NewFeedUser user;
    private String votableType;
    private Long votableId;
    @Enumerated(EnumType.STRING)
    private VoteType voteType;
}

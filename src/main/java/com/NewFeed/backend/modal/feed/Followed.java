package com.NewFeed.backend.modal.feed;

import com.NewFeed.backend.modal.BaseModel;
import com.NewFeed.backend.modal.user.NewFeedUser;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Followed extends BaseModel {
    @ManyToOne
    protected NewFeedUser user;
    @ManyToOne
    protected NewFeedUser followedUser;
}

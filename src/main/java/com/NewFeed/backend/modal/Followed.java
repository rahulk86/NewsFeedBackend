package com.NewFeed.backend.modal;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Followed extends BaseModel{
    @ManyToOne
    protected NewFeedUser user;
    @ManyToOne
    protected NewFeedUser followedUser;
}

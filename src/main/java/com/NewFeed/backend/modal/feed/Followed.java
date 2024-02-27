package com.NewFeed.backend.modal.feed;

import com.auth.modal.BaseModel;
import com.auth.modal.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Followed extends BaseModel {
    @ManyToOne
    protected User user;
    @ManyToOne
    protected User followedUser;
}

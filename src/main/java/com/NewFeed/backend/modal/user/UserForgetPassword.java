package com.NewFeed.backend.modal.user;

import com.NewFeed.backend.modal.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UserForgetPassword extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private NewFeedUser user;
    @Column(nullable = false, unique = true)
    private String verificationToken;

}

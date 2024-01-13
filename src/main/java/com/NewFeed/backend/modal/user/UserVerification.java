package com.NewFeed.backend.modal.user;

import com.NewFeed.backend.modal.BaseModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UserVerification extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private NewFeedUser user;
    @Column(nullable = false, unique = true)
    private String verificationToken;
    private String confirmationCode;

}

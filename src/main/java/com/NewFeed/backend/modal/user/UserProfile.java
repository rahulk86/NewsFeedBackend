package com.NewFeed.backend.modal.user;

import com.NewFeed.backend.modal.image.Imageable;
import com.auth.modal.BaseModel;
import com.auth.modal.user.GenderType;
import com.auth.modal.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UserProfile extends BaseModel implements Imageable {
    @OneToOne
    private User user;
    private String address;
    private int phoneNumber;
    @Enumerated(EnumType.STRING)
    private GenderType gender;

}

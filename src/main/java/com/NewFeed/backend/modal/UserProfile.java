package com.NewFeed.backend.modal;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UserProfile extends BaseModel implements Imageable{
    @OneToOne
    private NewFeedUser user;
    private String address;
    private int phoneNumber;
    @Enumerated(EnumType.STRING)
    private GenderType gender;

}

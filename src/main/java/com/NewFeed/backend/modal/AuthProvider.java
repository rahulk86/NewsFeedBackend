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
public class AuthProvider extends BaseModel{
    @OneToOne
    private NewFeedUser user;

    @Enumerated(EnumType.STRING)
    private AuthProviderType name;

    private String providerId ;
}

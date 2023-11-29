package com.NewFeed.backend.modal;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name = "user",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        })
public class NewFeedUser extends BaseModel {

    @NotBlank
    @NotBlank
    @Size(max = 20)
    private String name ;

    @NotBlank
    @Email
    @Size(max = 50)
    private String email ;

    @NotBlank
    @Size(max = 120)
    private String password ;
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "followedUser")
    private List<Followed> followings;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

}

package com.NewFeed.backend.modal;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "feedPost")
public class NewFeedPost extends NewFeedTextContent implements Votable,Imageable{
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "post")
    private List<NewFeedComment> comments;
}

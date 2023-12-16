package com.NewFeed.backend.modal.feed;

import com.NewFeed.backend.modal.image.Imageable;
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
public class NewFeedPost extends Replyable implements Votable, Imageable {
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "parent")
    private List<NewFeedComment> comments;
}

package com.NewFeed.backend.modal.feed;

import com.NewFeed.backend.modal.NewFeedTextContent;
import com.NewFeed.backend.modal.user.UserProfile;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Replyable extends NewFeedTextContent {
    @ManyToOne
    protected UserProfile userProfile;
}

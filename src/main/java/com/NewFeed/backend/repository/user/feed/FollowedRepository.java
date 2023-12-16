package com.NewFeed.backend.repository.user.feed;

import com.NewFeed.backend.modal.feed.Followed;
import com.NewFeed.backend.modal.user.NewFeedUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowedRepository extends JpaRepository<Followed,Long> {
    Optional<Followed> findByUserAndFollowedUser(NewFeedUser user,NewFeedUser followedUser);
}

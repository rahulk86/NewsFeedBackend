package com.NewFeed.backend.repository.feed;

import com.NewFeed.backend.modal.feed.Followed;
import com.auth.modal.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowedRepository extends JpaRepository<Followed,Long> {
    Optional<Followed> findByUserAndFollowedUser(User user, User followedUser);
}

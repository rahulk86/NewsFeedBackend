package com.NewFeed.backend.repository;

import com.NewFeed.backend.modal.Followed;
import com.NewFeed.backend.modal.NewFeedUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowedRepository extends JpaRepository<Followed,Long> {
    Optional<Followed> findByUserAndFollowedUser(NewFeedUser user,NewFeedUser followedUser);
}

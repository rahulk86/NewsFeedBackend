package com.NewFeed.backend.repository;

import com.NewFeed.backend.modal.NewFeedUser;
import com.NewFeed.backend.modal.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile,Long> {
    Optional<UserProfile> findByUser(NewFeedUser user);
    Optional<UserProfile> findByUserId(Long userId);

    @Query("select " +
                " userProfile , " +
                " image " +
            "from UserProfile userProfile " +
                "left join Image image on image.imageableId = userProfile.id and" +
                    " image.imageableType = 'UserProfile' and" +
                    " image.active = 1 " +
                "where userProfile.user = ?1" )
    Optional<Object[]> findByUserWithImage(NewFeedUser user);
}

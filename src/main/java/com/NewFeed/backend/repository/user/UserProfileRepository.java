package com.NewFeed.backend.repository.user;

import com.NewFeed.backend.modal.user.UserProfile;
import com.auth.modal.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile,Long> {
    Optional<UserProfile> findByUser(User user);
    Optional<UserProfile> findByUserId(Long userId);

    @Query("select " +
                " userProfile , " +
                " image " +
            "from UserProfile userProfile " +
                "left join Image image on image.imageableId = userProfile.id and" +
                    " image.imageableType = 'UserProfile' " +
                "where userProfile.user = ?1" )
    Optional<Object[]> findByUserWithImage(User user);

    @Query("select " +
            " userProfile , " +
            " image " +
            "from UserProfile userProfile " +
            "left join Image image on image.imageableId = userProfile.id and" +
            " image.imageableType = 'UserProfile' "  )
    List<Object[]> findAllByUserWithImage();
}

package com.NewFeed.backend.repository.messaging;

import com.NewFeed.backend.modal.messaging.GroupMessenger;
import com.NewFeed.backend.modal.user.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupMessengerRepository extends JpaRepository<GroupMessenger,Long> {
    @Query("select " +
            " messenger , " +
            " groupImage " +
            "from GroupMessenger messenger " +
            "inner join GroupMember member on " +
            " messenger.conversation = member.conversation and" +
            " member.profile = ?1 and" +
            " messenger.active = 1 and " +
            " member.active = 1 " +
            "left join Image groupImage on groupImage.imageableId = messenger.id and" +
            " groupImage.imageableType = 'GroupMessenger' and" +
            " groupImage.active = 1 " )
    List<Object[]> findAllByUser(UserProfile profile);
}

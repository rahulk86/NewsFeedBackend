package com.NewFeed.backend.repository.messaging;

import com.NewFeed.backend.modal.messaging.GroupConversation;
import com.NewFeed.backend.modal.messaging.GroupMessenger;
import com.NewFeed.backend.modal.user.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GroupMessengerRepository extends JpaRepository<GroupMessenger,Long> {
    @Query("select " +
            " messenger , " +
            " count(message) ," +
            " groupImage " +
            "from GroupMessenger messenger " +
                "inner join GroupMember member on " +
                    " messenger.conversation = member.conversation and" +
                    " member.profile = ?1 and" +
                    " messenger.active = 1 and " +
                    " member.active = 1 " +
                "left join UnreadGroupMessage message on " +
                    " message.member = member "+
                "left join Image groupImage on groupImage.imageableId = messenger.id and" +
                    " groupImage.imageableType = 'GroupMessenger' and" +
                    " groupImage.active = 1 "+
            "group by " +
              "messenger,groupImage")
    List<Object[]> findAllByUser(UserProfile profile);

    @Query("select COUNT(*) " +
            "from GroupMessenger messenger " +
                "inner join GroupMember member on " +
                    " messenger.conversation = member.conversation and" +
                    " member.profile = ?1 and" +
                    " messenger.active = 1 and " +
                    " member.active = 1 " +
                "left join UnreadGroupMessage message on " +
                    " message.member = member "+
            "group by " +
                 " messenger " +
            " having count(message) > 0")
    List<Integer>  countByProfile(UserProfile profile);

    Optional<GroupMessenger> findByConversation(GroupConversation conversation);
}

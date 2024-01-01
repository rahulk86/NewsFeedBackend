package com.NewFeed.backend.repository.messaging;

import com.NewFeed.backend.modal.messaging.GroupConversation;
import com.NewFeed.backend.modal.messaging.GroupMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupMessageRepository extends JpaRepository<GroupMessage,Long> {
    @Query("select " +
            " message ," +
            " profileImage " +
            "from GroupMessage message " +
                "left join Image profileImage on profileImage.imageableId = message.groupMember.profile.id and" +
                " profileImage.imageableType = 'UserProfile' and" +
                " profileImage.active = 1 " +
            " Where message.groupMember.conversation = ?1 " +
            " order by message.creatAt ")
    List<Object[]> findByGroupConversation(GroupConversation conversation);
}

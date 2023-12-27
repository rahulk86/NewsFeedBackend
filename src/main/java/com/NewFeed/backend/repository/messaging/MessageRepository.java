package com.NewFeed.backend.repository.messaging;

import com.NewFeed.backend.modal.messaging.GroupConversation;
import com.NewFeed.backend.modal.messaging.GroupMessage;
import com.NewFeed.backend.modal.messaging.UserConversation;
import com.NewFeed.backend.modal.messaging.UserMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<UserMessage,Long> {
    @Query("select " +
            " message " +
            "from UserMessage message " +
            " Where message.messenger.conversation = ?1 "+
            " order by message.creatAt ")
    List<UserMessage> findByUserConversation(UserConversation conversation);

    @Query("select " +
            " message " +
            "from GroupMessage message " +
            " Where message.groupMember.conversation = ?1 " +
            " order by message.creatAt ")
    List<GroupMessage> findByGroupConversation(GroupConversation conversation);
}

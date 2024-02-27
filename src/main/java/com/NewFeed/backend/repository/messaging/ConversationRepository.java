package com.NewFeed.backend.repository.messaging;

import com.NewFeed.backend.modal.messaging.Conversation;
import com.NewFeed.backend.modal.messaging.UserConversation;
import com.NewFeed.backend.modal.user.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation,Long> {

    @Query("select " +
            " reciverMessenger.conversation  " +
            "from UserMessenger messenger " +
            "inner join UserMessenger reciverMessenger on " +
            " messenger.conversation = reciverMessenger.conversation and" +
            " messenger.profile = ?1 and" +
            " reciverMessenger.profile = ?2 " )
    Optional<UserConversation> findBySenderAndReceiver(
            @Param("sender") UserProfile sender,
            @Param("receiver") UserProfile receiver
    );

}

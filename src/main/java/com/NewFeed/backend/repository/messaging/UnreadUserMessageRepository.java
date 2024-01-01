package com.NewFeed.backend.repository.messaging;

import com.NewFeed.backend.modal.messaging.UnreadUserMessage;
import com.NewFeed.backend.modal.messaging.UserMessenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface UnreadUserMessageRepository extends JpaRepository<UnreadUserMessage,Long> {
    Long countByMessenger(UserMessenger messenger);
    @Modifying
    void deleteUnreadUserMessageByMessenger(UserMessenger messenger);
}

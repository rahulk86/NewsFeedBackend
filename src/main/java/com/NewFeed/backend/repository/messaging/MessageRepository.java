package com.NewFeed.backend.repository.messaging;

import com.NewFeed.backend.modal.messaging.Message;
import com.NewFeed.backend.modal.messaging.Messenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Long> {
    List<Message> findByMessenger(Messenger messenger);
}

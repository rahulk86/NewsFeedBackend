package com.NewFeed.backend.repository.messaging;

import com.NewFeed.backend.modal.messaging.Messenger;
import com.NewFeed.backend.modal.messaging.UserMessenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserMessengerRepository extends JpaRepository<UserMessenger,Long> {
    Optional<UserMessenger> findByMessenger(Messenger messenger);
}

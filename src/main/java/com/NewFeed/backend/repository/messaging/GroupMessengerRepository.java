package com.NewFeed.backend.repository.messaging;

import com.NewFeed.backend.modal.messaging.GroupMessenger;
import com.NewFeed.backend.modal.messaging.Messenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupMessengerRepository extends JpaRepository<GroupMessenger,Long> {
    Optional<GroupMessenger> findByMessenger(Messenger messenger);
}

package com.NewFeed.backend.repository.messaging;

import com.NewFeed.backend.modal.messaging.GroupMessenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMessengerRepository extends JpaRepository<GroupMessenger,Long> {
//    Optional<GroupMessenger> findByMessenger(Messenger messenger);
}

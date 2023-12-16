package com.NewFeed.backend.repository.messaging;

import com.NewFeed.backend.modal.messaging.Messenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessengerRepository extends JpaRepository<Messenger,Long> {

}

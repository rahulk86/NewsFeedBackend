package com.NewFeed.backend.repository.messaging;

import com.NewFeed.backend.modal.messaging.GroupMember;
import com.NewFeed.backend.modal.messaging.UnreadGroupMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface UnreadGroupMessageRepository extends JpaRepository<UnreadGroupMessage,Long> {
    Long countByMember(GroupMember member);
    @Modifying
    void deleteByMember(GroupMember member);
}

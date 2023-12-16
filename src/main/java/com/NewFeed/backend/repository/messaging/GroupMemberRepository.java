package com.NewFeed.backend.repository.messaging;

import com.NewFeed.backend.modal.messaging.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberRepository extends JpaRepository<GroupMember,Long> {
}

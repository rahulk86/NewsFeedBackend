package com.NewFeed.backend.repository.messaging;

import com.NewFeed.backend.modal.messaging.GroupConversation;
import com.NewFeed.backend.modal.messaging.GroupMember;
import com.NewFeed.backend.modal.user.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember,Long> {
    List<GroupMember> findByConversation(GroupConversation conversation);
    Optional<GroupMember> findByConversationAndProfile(GroupConversation conversation, UserProfile profile);
}

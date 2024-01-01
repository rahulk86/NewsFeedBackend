package com.NewFeed.backend.service.impl;

import com.NewFeed.backend.dto.GroupMemberDto;
import com.NewFeed.backend.modal.messaging.GroupMember;
import com.NewFeed.backend.modal.user.UserProfile;

public interface GroupMemberService {
    GroupMember groupMember(Long messengerId, UserProfile profile);
    GroupMemberDto getGroupMember(Long messengerId, UserProfile profile);
}

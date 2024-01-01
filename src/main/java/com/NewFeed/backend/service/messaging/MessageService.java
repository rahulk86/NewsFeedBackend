package com.NewFeed.backend.service.messaging;

import com.NewFeed.backend.dto.GroupMessageDto;
import com.NewFeed.backend.dto.MessengerDto;
import com.NewFeed.backend.dto.UserMessageDto;
import com.NewFeed.backend.modal.messaging.GroupMember;
import com.NewFeed.backend.modal.user.UserProfile;
import com.NewFeed.backend.payload.Response.UserMessageResponse;

import java.util.List;

public interface MessageService {
    List<GroupMessageDto> getGroupMessages(MessengerDto messenger);
    List<UserMessageDto> getUserMessages(UserProfile profile,MessengerDto messenger);

    UserMessageResponse creatUserMessage(Long messengerId, UserProfile profile, UserMessageDto userMessageDto);
    GroupMessageDto creatGroupMessage(GroupMember groupMember, GroupMessageDto groupMessage);

}

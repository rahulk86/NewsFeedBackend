package com.NewFeed.backend.service.messaging;

import com.NewFeed.backend.dto.GroupMessageDto;
import com.NewFeed.backend.dto.MessengerDto;
import com.NewFeed.backend.dto.UserMessageDto;
import com.NewFeed.backend.modal.user.UserProfile;

import java.util.List;

public interface MessageService {
    List<GroupMessageDto> getGroupMessages(MessengerDto messenger);
    List<UserMessageDto> getUserMessages(MessengerDto messenger);

    UserMessageDto creatUserMessage(Long messengerId, UserProfile profile,UserMessageDto userMessageDto);

}

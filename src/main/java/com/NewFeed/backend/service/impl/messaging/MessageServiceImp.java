package com.NewFeed.backend.service.impl.messaging;

import com.NewFeed.backend.dto.GroupMessageDto;
import com.NewFeed.backend.dto.MessengerDto;
import com.NewFeed.backend.dto.UserMessageDto;
import com.NewFeed.backend.exception.MessageException;
import com.NewFeed.backend.modal.messaging.*;
import com.NewFeed.backend.modal.user.UserProfile;
import com.NewFeed.backend.repository.messaging.ConversationRepository;
import com.NewFeed.backend.repository.messaging.GroupMessengerRepository;
import com.NewFeed.backend.repository.messaging.MessageRepository;
import com.NewFeed.backend.repository.messaging.UserMessengerRepository;
import com.NewFeed.backend.service.messaging.MessageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImp implements MessageService {


    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private GroupMessengerRepository messengerRepository;

    @Autowired
    private UserMessengerRepository userMessengerRepository;

    @Autowired
    @Qualifier("messageConfigModelMapper")
    private ModelMapper messageModelMapper;
    @Override
    public List<GroupMessageDto> getGroupMessages(MessengerDto messengerDto) {
        GroupMessenger messenger =  messengerRepository
                                .findById(messengerDto.getId())
                                .orElseThrow(()->new MessageException("MessageException : messenger not fount with given id : "+messengerDto.getId()));
        return messageRepository
                .findByGroupConversation(messenger.getConversation())
                .stream()
                .map(message->messageModelMapper.map(message, GroupMessageDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserMessageDto> getUserMessages(MessengerDto messengerDto) {
        UserMessenger messenger =  userMessengerRepository
                                    .findById(messengerDto.getId())
                                    .orElseThrow(()->new MessageException("MessageException : messenger not fount with given id : "+messengerDto.getId()));
        return messageRepository
                .findByUserConversation(messenger.getConversation())
                .stream()
                .map(message->messageModelMapper.map(message, UserMessageDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserMessageDto creatUserMessage(Long conversationId, UserProfile profile,UserMessageDto userMessageDto) {
        UserConversation conversation = conversationRepository
                                        .findById(conversationId)
                                        .orElseThrow(() -> new MessageException("MessageException : conversation not fount with given id : " + conversationId));
        UserMessenger sender = userMessengerRepository
                                        .findByProfileAndConversation(profile, conversation)
                                        .orElseThrow(() -> new MessageException("MessageException : messenger not fount with given profile id : " + profile.getId()));

        UserMessage userMessage = new UserMessage();
        userMessage.setMessenger(sender);
        userMessage.setText(userMessageDto.getText());
        return messageModelMapper.map(messageRepository.save(userMessage),UserMessageDto.class);
    }
}

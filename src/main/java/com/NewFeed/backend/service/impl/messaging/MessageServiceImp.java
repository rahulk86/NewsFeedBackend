package com.NewFeed.backend.service.impl.messaging;

import com.NewFeed.backend.configuration.security.AppProperties;
import com.NewFeed.backend.dto.*;
import com.NewFeed.backend.exception.MessageException;
import com.NewFeed.backend.modal.image.Image;
import com.NewFeed.backend.modal.messaging.*;
import com.NewFeed.backend.modal.user.UserProfile;
import com.NewFeed.backend.payload.Response.UserMessageResponse;
import com.NewFeed.backend.repository.messaging.*;
import com.NewFeed.backend.service.messaging.MessageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MessageServiceImp implements MessageService {


    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private GroupMessageRepository groupMessageRepository;

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private GroupMessengerRepository messengerRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private UnreadUserMessageRepository unreadUserMessageRepository;

    @Autowired
    private UserMessengerRepository userMessengerRepository;
    @Autowired
    private UnreadGroupMessageRepository unreadGroupMessageRepository;

    @Autowired
    @Qualifier("messageConfigModelMapper")
    private ModelMapper messageModelMapper;

    @Autowired
    @Qualifier("messengerConfigModelMapper")
    private ModelMapper messengerModelMapper;

    @Autowired
    @Qualifier("userImageServiceModelMapper")
    private ModelMapper imageModelMapper;
    @Override
    @Transactional
    public List<GroupMessageDto> getGroupMessages(MessengerDto messengerDto) {
        GroupMessenger messenger =  messengerRepository
                                .findById(messengerDto.getId())
                                .orElseThrow(()->new MessageException("MessageException : messenger not fount with given id : "+messengerDto.getId()));
        return groupMessageRepository
                    .findByGroupConversation(messenger.getConversation())
                    .stream()
                    .map(this::toGroupMessageDto)
                    .collect(Collectors.toList());
    }

    private GroupMessageDto toGroupMessageDto(Object[] message){
        GroupMessage groupMessage = (GroupMessage) message[0];
        GroupMessageDto messageDto = messageModelMapper.map(groupMessage, GroupMessageDto.class);
        messageDto.setGroupMember(messengerModelMapper.map(groupMessage.getGroupMember(),GroupMemberDto.class));
        if(message.length>1 && (Image)message[1] != null) {
            messageDto.getGroupMember().setImage(imageModelMapper.map((Image)message[1],ImageDto.class));
        }
        return messageDto;
    }

    @Override
    @Transactional
    public List<UserMessageDto> getUserMessages(UserProfile profile,MessengerDto messengerDto) {
        UserMessenger messenger =  userMessengerRepository
                                    .findById(messengerDto.getId())
                                    .orElseThrow(()->new MessageException("MessageException : messenger not fount with given id : "+messengerDto.getId()));
        UserMessenger userMessenger = userMessengerRepository
                                        .findByProfileAndConversation(profile, messenger.getConversation())
                                        .orElse(null);
        unreadUserMessageRepository.deleteUnreadUserMessageByMessenger(userMessenger);
        return messageRepository
                .findByUserConversation(messenger.getConversation())
                .stream()
                .map(message->messageModelMapper.map(message, UserMessageDto.class))
                .collect(Collectors.toList());

    }

    @Override
    @Transactional
    public UserMessageResponse creatUserMessage(Long conversationId, UserProfile profile,UserMessageDto userMessageDto) {
        UserConversation conversation = (UserConversation)conversationRepository
                                                            .findById(conversationId)
                                                            .orElseThrow(() -> new MessageException("MessageException : conversation not fount with given id : " + conversationId));
        UserMessenger sender = userMessengerRepository
                                        .findByProfileAndConversation(profile, conversation)
                                        .orElseThrow(() -> new MessageException("MessageException : messenger not fount with given profile id : " + profile.getId()));
        sender.setCreatAt(appProperties.now());
        UserMessenger userMessenger = userMessengerRepository.save(sender);
        unreadUserMessageRepository.
                deleteUnreadUserMessageByMessenger(userMessenger);

        UserMessage userMessage = messageModelMapper.map(userMessageDto,UserMessage.class);
        userMessage.setMessenger(sender);
        userMessage.setCreatAt(appProperties.now());
        UserMessage message = messageRepository.save(userMessage);


         userMessengerRepository
                .findByConversation(sender.getConversation())
                .stream()
                .filter(messenger -> !Objects.equals(messenger.getId(), userMessenger.getId()))
                 .forEach((messenger)->{
                    UnreadUserMessage unreadUserMessage = new UnreadUserMessage();
                    unreadUserMessage.setMessenger(messenger);
                    unreadUserMessage.setMessage(message);
                    unreadUserMessage.setCreatAt(appProperties.now());
                    unreadUserMessage.setActive(1);
                    unreadUserMessageRepository.save(unreadUserMessage);
                 });

        MessengerDto messengerDto  = messengerModelMapper.map(userMessenger, MessengerDto.class);
        messengerDto.setType(UserMessenger.class.getSimpleName());

        return UserMessageResponse
                .builder()
                .message(messageModelMapper.map(message,UserMessageDto.class))
                .messenger(messengerDto)
                .build();
    }

    @Override
    @Transactional
    public GroupMessageDto creatGroupMessage(GroupMember groupMember, GroupMessageDto groupMessage) {
        GroupMessage message = messageModelMapper.map(groupMessage,GroupMessage.class);
        message.setGroupMember(groupMember);
        message.setCreatAt(appProperties.now());
        GroupMessage message1 = groupMessageRepository.save(message);

        groupMemberRepository.findByConversation(groupMember.getConversation())
                .stream()
                .filter(member -> !Objects.equals(member.getId(), groupMember.getId()))
                .forEach(member->{
                    UnreadGroupMessage unreadGroupMessage = new UnreadGroupMessage();
                    unreadGroupMessage.setActive(1);
                    unreadGroupMessage.setCreatAt(appProperties.now());
                    unreadGroupMessage.setMember(member);
                    unreadGroupMessage.setMessage(message1);
                    unreadGroupMessageRepository.save(unreadGroupMessage);
                });

        GroupMessageDto messageDto = messageModelMapper.map(groupMessageRepository.save(message), GroupMessageDto.class);
        messageDto.setGroupMember(messengerModelMapper.map(groupMember, GroupMemberDto.class));
        return messageDto;
    }

}

package com.NewFeed.backend.service.impl.messaging;

import com.NewFeed.backend.dto.GroupMessageDto;
import com.NewFeed.backend.dto.MessengerDto;
import com.NewFeed.backend.dto.UserMessageDto;
import com.NewFeed.backend.exception.MessageException;
import com.NewFeed.backend.modal.messaging.Messenger;
import com.NewFeed.backend.repository.messaging.MessageRepository;
import com.NewFeed.backend.repository.messaging.MessengerRepository;
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
    private MessengerRepository messengerRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    @Qualifier("messageConfigModelMapper")
    private ModelMapper messageModelMapper;
    @Override
    public List<GroupMessageDto> getGroupMessages(MessengerDto messengerDto) {
        Messenger messenger =  messengerRepository
                                .findById(messengerDto.getId())
                                .orElseThrow(()->new MessageException("MessageException : messenger not fount with given id : "+messengerDto.getId()));
        return messageRepository
                .findByMessenger(messenger)
                .stream()
                .map(message->messageModelMapper.map(message, GroupMessageDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserMessageDto> getUserMessages(MessengerDto messengerDto) {
        Messenger messenger =  messengerRepository
                                .findById(messengerDto.getId())
                                .orElseThrow(()->new MessageException("MessageException : messenger not fount with given id : "+messengerDto.getId()));
        return messageRepository
                .findByMessenger(messenger)
                .stream()
                .map(message->messageModelMapper.map(message, UserMessageDto.class))
                .collect(Collectors.toList());
    }
}

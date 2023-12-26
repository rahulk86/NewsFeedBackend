package com.NewFeed.backend.service.impl.messaging;

import com.NewFeed.backend.dto.ImageDto;
import com.NewFeed.backend.dto.MessengerDto;
import com.NewFeed.backend.dto.UserProfileDto;
import com.NewFeed.backend.modal.image.Image;
import com.NewFeed.backend.modal.messaging.*;
import com.NewFeed.backend.modal.user.UserProfile;
import com.NewFeed.backend.repository.messaging.*;
import com.NewFeed.backend.service.messaging.MessengerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MessengerServiceImpl implements MessengerService {
    @Autowired
    private MessengerRepository messengerRepository;
    @Autowired
    private GroupMemberRepository groupMemberRepository;
    @Autowired
    private GroupMessengerRepository groupMessengerRepository;
    @Autowired
    private UserMessengerRepository userMessengerRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    @Qualifier("messengerConfigModelMapper")
    private ModelMapper messengerModelMapper;

    @Autowired
    @Qualifier("userImageServiceModelMapper")
    private ModelMapper imageModelMapper;
    @Override
    public List<MessengerDto> getMessengers(UserProfile user) {
//        List<MessengerDto> messengers = groupMessengerRepository
//                                            .findAll()
//                                            .stream()
//                                            .map(messenger -> messengerModelMapper.map(messenger, MessengerDto.class))
//                                            .collect(Collectors.toList());
        return  userMessengerRepository
                    .findAllByUser(user)
                    .stream()
                    .map(this::toSenderMessengerDto)
                    .toList();
//        return messengers;
    }


//    private MessengerDto toMessengerDto(Object[] messenger){
//        UserMessenger userMessenger = (UserMessenger) messenger[0];
//        MessengerDto messengerDto = messengerModelMapper.map(userMessenger, MessengerDto.class);
//        messengerDto.setName(userMessenger.getSender().getUser().getName());
//        if(messenger.length>1 && (Image)messenger[1] != null) {
//            messengerDto.setImage(imageModelMapper.map((Image)messenger[1],ImageDto.class));
//        }
//        return messengerDto;
//    }

    private MessengerDto toSenderMessengerDto(Object[] messenger){
        UserMessenger userMessenger = (UserMessenger) messenger[0];
        MessengerDto messengerDto = messengerModelMapper.map(userMessenger, MessengerDto.class);
        if(messenger.length>1 && (Image)messenger[1] != null) {
            messengerDto.setImage(imageModelMapper.map((Image)messenger[1],ImageDto.class));
        }
        return messengerDto;
    }
    private Messenger createMessenger(MessengerType messengerType){
        Messenger messenger = new Messenger();
        messenger.setActive(1);
        messenger.setCreatAt(LocalDateTime.now());
        messenger.setMessengerId(messengerType.getId());
        messenger.setMessengerType(messengerType.getClass().getSimpleName());
        return messengerRepository.save(messenger);
    }
    @Override
    public MessengerDto createUserMessenger(UserProfile sender,UserProfile receiver) {
        UserConversation conversation = conversationRepository
                                          .findBySenderAndReceiver(sender,receiver)
                                          .orElse(conversationRepository.save(new UserConversation()));

        UserMessenger userMessenger  =  userMessengerRepository
                .findByProfileAndConversation (receiver,conversation)
                .orElseGet(()->{
                    UserMessenger senderMessenger= new UserMessenger();
                    senderMessenger.setConversation(conversation);
                    senderMessenger.setProfile(sender);
                    userMessengerRepository.save(senderMessenger);

                    UserMessenger receiverMessenger= new UserMessenger();
                    receiverMessenger.setConversation(conversation);
                    receiverMessenger.setProfile(receiver);
                    return userMessengerRepository.save(receiverMessenger);

                });
        return  messengerModelMapper.map(userMessenger, MessengerDto.class);
    }

    private GroupMember createUserRoleGroupMember(UserProfile sender,GroupMessenger receiver){
        GroupMember groupMember = new GroupMember();
            groupMember.setRole(GroupRole.ROLE_USER);
            groupMember.setProfile(sender);
            groupMember.setGroupMessenger(receiver);
        return groupMemberRepository.save(groupMember);
    }
    @Override
    public GroupMessenger createGroupMessenger(UserProfile sender,UserProfileDto receiver) {
        GroupMessenger groupMessenger = new GroupMessenger();
        groupMessenger.setGroupMembers(new ArrayList<>());
        groupMessenger.setName(receiver.getUser().getName());
        GroupMessenger save = groupMessengerRepository.save(groupMessenger);
        save.setMessenger(createMessenger(save));
        save.getGroupMembers().add(createUserRoleGroupMember(sender,groupMessenger));
        return groupMessenger;
    }


}

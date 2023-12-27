package com.NewFeed.backend.service.impl.messaging;

import com.NewFeed.backend.dto.ImageDto;
import com.NewFeed.backend.dto.MessengerDto;
import com.NewFeed.backend.dto.UserProfileDto;
import com.NewFeed.backend.modal.image.Image;
import com.NewFeed.backend.modal.messaging.*;
import com.NewFeed.backend.modal.user.UserProfile;
import com.NewFeed.backend.repository.messaging.ConversationRepository;
import com.NewFeed.backend.repository.messaging.GroupMemberRepository;
import com.NewFeed.backend.repository.messaging.GroupMessengerRepository;
import com.NewFeed.backend.repository.messaging.UserMessengerRepository;
import com.NewFeed.backend.service.messaging.MessengerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MessengerServiceImpl implements MessengerService {
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
        List<MessengerDto> messengers = groupMessengerRepository
                                            .findAll()
                                            .stream()
                                            .map(messenger -> messengerModelMapper.map(messenger, MessengerDto.class))
                                            .collect(Collectors.toList());
        messengers.addAll(userMessengerRepository
                    .findAllByUser(user)
                    .stream()
                    .map(this::toSenderMessengerDto)
                    .toList());
        return messengers;
    }



    private MessengerDto toSenderMessengerDto(Object[] messenger){
        UserMessenger userMessenger = (UserMessenger) messenger[0];
        MessengerDto messengerDto = messengerModelMapper.map(userMessenger, MessengerDto.class);
        if(messenger.length>1 && (Image)messenger[1] != null) {
            messengerDto.setImage(imageModelMapper.map((Image)messenger[1],ImageDto.class));
        }
        return messengerDto;
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
            groupMember.setConversation(receiver.getConversation());
        return groupMemberRepository.save(groupMember);
    }
    @Override
    public GroupMessenger createGroupMessenger(UserProfile sender,UserProfileDto receiver) {
        GroupMessenger groupMessenger = new GroupMessenger();
//        groupMessenger.setGroupMembers(new ArrayList<>());
        groupMessenger.setName(receiver.getUser().getName());
        GroupMessenger save = groupMessengerRepository.save(groupMessenger);
//        save.setMessenger(createMessenger(save));
//        save.getGroupMembers().add(createUserRoleGroupMember(sender,groupMessenger));
        return groupMessenger;
    }


}

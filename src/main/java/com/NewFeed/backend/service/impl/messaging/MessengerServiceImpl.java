package com.NewFeed.backend.service.impl.messaging;

import com.NewFeed.backend.configuration.security.AppProperties;
import com.NewFeed.backend.dto.ImageDto;
import com.NewFeed.backend.dto.MessengerDto;
import com.NewFeed.backend.exception.MessengerException;
import com.NewFeed.backend.modal.image.Image;
import com.NewFeed.backend.modal.messaging.*;
import com.NewFeed.backend.modal.user.UserProfile;
import com.NewFeed.backend.payload.Response.UpdateMessengerResponse;
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
    private AppProperties appProperties;

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
                                            .findAllByUser(user)
                                            .stream()
                                            .map(this::grouptoSenderMessengerDto)
                                            .collect(Collectors.toList());

        messengers.addAll(userMessengerRepository
                    .findAllByUser(user)
                    .stream()
                    .map(this::toSenderMessengerDto)
                    .toList());
        return messengers;
    }
   @Override
   public Integer unreadMessenger(UserProfile profile){
      return userMessengerRepository.countByProfile(profile);
   }

    private MessengerDto toSenderMessengerDto(Object[] messenger){
        UserMessenger userMessenger = (UserMessenger) messenger[0];
        MessengerDto messengerDto = messengerModelMapper.map(userMessenger, MessengerDto.class);
        messengerDto.setType(UserMessenger.class.getSimpleName());
        buildMessengerImageCount(messengerDto,messenger);
        return messengerDto;
    }
    private void buildMessengerImageCount(MessengerDto messengerDto,Object[] messenger){
        if(messenger.length>2 && (Long)messenger[1] != null) {
            messengerDto.setUnreadMessages((Long)messenger[1]);
            if((Image)messenger[2]!=null) {
                messengerDto.setImage(imageModelMapper.map((Image) messenger[2], ImageDto.class));
            }
        }else if(messenger.length>1 && messenger[1] instanceof Image) {
            messengerDto.setImage(imageModelMapper.map((Image)messenger[1],ImageDto.class));
        }else if(messenger.length>1 ) {
            messengerDto.setUnreadMessages((Long)messenger[1]);
        }
    }
    private MessengerDto grouptoSenderMessengerDto(Object[] messenger){
        GroupMessenger userMessenger = (GroupMessenger) messenger[0];
        MessengerDto messengerDto = messengerModelMapper.map(userMessenger, MessengerDto.class);
        messengerDto.setType(GroupMessenger.class.getSimpleName());
        buildMessengerImageCount(messengerDto,messenger);
        return messengerDto;
    }

    @Transactional
    @Override
    public MessengerDto createUserMessenger(UserProfile sender,UserProfile receiver) {
        UserConversation conversation = conversationRepository
                                          .findBySenderAndReceiver(sender,receiver)
                                          .orElseGet(()->conversationRepository.save(messengerModelMapper.map(sender,UserConversation.class)));

        UserMessenger userMessenger  =  userMessengerRepository
                .findByProfileAndConversation (receiver,conversation)
                .orElseGet(()->{
                    conversation.setCreatAt(appProperties.now());
                    UserMessenger senderMessenger= messengerModelMapper.map(sender,UserMessenger.class);
                    senderMessenger.setProfile(sender);
                    senderMessenger.setConversation(conversation);
                    senderMessenger.setCreatAt(appProperties.now());
                    userMessengerRepository.save(senderMessenger);

                    UserMessenger receiverMessenger= messengerModelMapper.map(receiver,UserMessenger.class);
                    receiverMessenger.setProfile(receiver);
                    receiverMessenger.setConversation(conversation);
                    receiverMessenger.setCreatAt(appProperties.now());
                    return userMessengerRepository.save(receiverMessenger);

                });
        MessengerDto messengerDto = messengerModelMapper.map(userMessenger, MessengerDto.class);
        messengerDto.setType(UserMessenger.class.getSimpleName());
        return  messengerDto;
    }

    private void createGroupMember(UserProfile profile,
                                  GroupConversation conversationSave,
                                  GroupRole role){
        GroupMember member = new GroupMember();
        member.setConversation(conversationSave);
        member.setRole(role);
        member.setProfile(profile);
        member.setCreatAt(appProperties.now());
        member.setActive(1);
        groupMemberRepository.save(member);
    }
    @Transactional
    @Override
    public MessengerDto createGroupMessenger(UserProfile sender,MessengerDto messenger,List<UserProfile> receiver) {

        GroupConversation conversation = new GroupConversation();
        conversation.setCreatAt(appProperties.now());
        conversation.setActive(1);
        GroupConversation conversationSave = conversationRepository.save(conversation);

        GroupMessenger groupMessenger = new GroupMessenger();
        groupMessenger.setName(messenger.getName());
        groupMessenger.setConversation(conversationSave);
        groupMessenger.setCreatAt(appProperties.now());
        groupMessenger.setActive(1);

        createGroupMember(sender,conversationSave,GroupRole.ROLE_ADMIN);

        receiver.forEach(profile->{
            createGroupMember(profile,conversationSave,GroupRole.ROLE_USER);
        });

        MessengerDto messengerDto = messengerModelMapper
                                        .map(groupMessengerRepository.save(groupMessenger),
                                             MessengerDto.class);
        messengerDto.setType(GroupMessenger.class.getSimpleName());
        return messengerDto;
    }

    @Override
    @Transactional
    public UpdateMessengerResponse updateMessengerTime(UserProfile profile, MessengerDto messenger) {
        UserMessenger userMessenger = userMessengerRepository
                                            .findById(messenger.getId())
                                            .orElseThrow(() -> new MessengerException("MessengerException :: messenger not found with given id " + messenger.getId()));
        UserMessenger loginUserMessenger = userMessengerRepository
                                            .findByProfileAndConversation(profile, userMessenger.getConversation())
                                            .orElse(null);
        if(loginUserMessenger!=null) {
            loginUserMessenger.setCreatAt(appProperties.now());
            userMessengerRepository.save(loginUserMessenger);
        }

        MessengerDto receiver = messengerModelMapper.map(userMessenger,MessengerDto.class);
        receiver.setType(UserMessenger.class.getSimpleName());

        MessengerDto sender = messengerModelMapper.map(loginUserMessenger,MessengerDto.class);
        sender.setType(UserMessenger.class.getSimpleName());

        return UpdateMessengerResponse
                            .builder()
                            .sender(sender)
                            .receiver(receiver)
                            .build();
    }


}

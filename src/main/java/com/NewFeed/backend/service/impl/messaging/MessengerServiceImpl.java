package com.NewFeed.backend.service.impl.messaging;

import com.NewFeed.backend.dto.MessengerDto;
import com.NewFeed.backend.dto.UserDto;
import com.NewFeed.backend.dto.UserProfileDto;
import com.NewFeed.backend.modal.messaging.*;
import com.NewFeed.backend.modal.user.UserProfile;
import com.NewFeed.backend.repository.messaging.GroupMemberRepository;
import com.NewFeed.backend.repository.messaging.GroupMessengerRepository;
import com.NewFeed.backend.repository.messaging.MessengerRepository;
import com.NewFeed.backend.repository.messaging.UserMessengerRepository;
import com.NewFeed.backend.service.messaging.MessengerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Qualifier("messengerConfigModelMapper")
    private ModelMapper messengerModelMapper;
    @Override
    public List<MessengerDto> getMessengers(UserDto user) {
        List<MessengerDto> messengers = groupMessengerRepository
                                            .findAll()
                                            .stream()
                                            .map(messenger -> messengerModelMapper.map(messenger, MessengerDto.class))
                                            .collect(Collectors.toList());
        messengers.addAll(userMessengerRepository
                                    .findAll()
                                    .stream()
                                    .map(messenger -> messengerModelMapper.map(messenger, MessengerDto.class))
                                    .toList());
        return messengers;
    }

    private Messenger createMessenger(MessengerType messengerType){
        Messenger messenger = new Messenger();
        messenger.setMessengerId(messengerType.getId());
        messenger.setMessengerType(messengerType.getClass().getSimpleName());
        return messengerRepository.save(messenger);
    }
    @Override
    public void createUserMessenger( UserProfile sender,UserProfile receiver) {
        UserMessenger userMessenger = new UserMessenger();
        userMessenger.setSender(sender);
        userMessenger.setReceiver(receiver);
        UserMessenger userMessengerSave = userMessengerRepository.save(userMessenger);
        userMessengerSave.setMessenger(createMessenger(userMessengerSave));
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

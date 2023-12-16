package com.NewFeed.backend.controller;

import com.NewFeed.backend.dto.*;
import com.NewFeed.backend.modal.messaging.GroupMessenger;
import com.NewFeed.backend.modal.user.UserProfile;
import com.NewFeed.backend.payload.Response.MessageResponse;
import com.NewFeed.backend.service.UserProfileService;
import com.NewFeed.backend.service.messaging.MessageService;
import com.NewFeed.backend.service.messaging.MessengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/messenger")
public class MessengerController {
    @Autowired
    private MessengerService messengerService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserProfileService profileService;
    @GetMapping("/get")
    public ResponseEntity<?> getMessengers(Authentication authentication ){

        List<MessengerDto> messengers = messengerService.getMessengers((UserDto) authentication.getPrincipal());
        return ResponseEntity.ok(messengers);

    }
    @PostMapping("/createUserMessenger")
    public ResponseEntity<?> createUserMessenger(Authentication authentication , UserProfileDto profile){
        UserProfile sender = profileService.getUserProfile((UserDto) authentication.getPrincipal());
        UserProfile receiver = profileService.getUserProfile(profile.getUser());
        messengerService.createUserMessenger(sender,receiver);
        return new ResponseEntity<>(MessageResponse
                                            .builder()
                                            .message("UserMessenger created successfully")
                                            .build()
                                    , HttpStatus.CREATED);
    }

    @PostMapping("/createGroup")
    public ResponseEntity<?> createGroup(Authentication authentication , UserProfileDto profile){
        UserProfile sender = profileService.getUserProfile((UserDto) authentication.getPrincipal());
        GroupMessenger groupMessenger = messengerService.createGroupMessenger(sender, profile);
        return ResponseEntity.ok(groupMessenger);
    }
    @GetMapping("/userConversation")
    public ResponseEntity<?> startUserConversation(MessengerDto messengerDto,Authentication authentication ){
        UserDto principal = (UserDto) authentication.getPrincipal();
        List<UserMessageDto> userMessages = messageService.getUserMessages(messengerDto);
        return ResponseEntity.ok(userMessages);
    }

    @GetMapping("/groupConversation")
    public ResponseEntity<?> startGroupConversation(MessengerDto messengerDto,Authentication authentication ){
        UserDto principal = (UserDto) authentication.getPrincipal();
        List<GroupMessageDto> groupMessages = messageService.getGroupMessages(messengerDto);
        return ResponseEntity.ok(groupMessages);
    }
}

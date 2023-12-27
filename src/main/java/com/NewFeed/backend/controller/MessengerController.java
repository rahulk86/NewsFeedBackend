package com.NewFeed.backend.controller;

import com.NewFeed.backend.dto.*;
import com.NewFeed.backend.modal.messaging.GroupMessenger;
import com.NewFeed.backend.modal.user.UserProfile;
import com.NewFeed.backend.service.UserProfileService;
import com.NewFeed.backend.service.messaging.MessageService;
import com.NewFeed.backend.service.messaging.MessengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
        UserProfile user = profileService.getUserProfile((UserDto) authentication.getPrincipal());
        List<MessengerDto> messengers = messengerService.getMessengers(user);
        return ResponseEntity.ok(messengers);

    }
    @MessageMapping("/sendMessage/{conversationId}")
    @SendTo("/topic/conversation/{conversationId}")
    public ResponseEntity<?> sendMessage(Principal principal,
                                         @DestinationVariable Long conversationId,
                                         @RequestBody UserMessageDto message) {
        UserProfile profile = profileService.getUserProfile((UserDto) principal);
        UserMessageDto userMessageDto = messageService.creatUserMessage(conversationId,profile,message);
        return ResponseEntity.ok(userMessageDto);
    }

    @PostMapping("/getUserMessage")
    public ResponseEntity<?> getUserMessage(@RequestBody MessengerDto messenger){
        List<UserMessageDto> userMessages = messageService.getUserMessages(messenger);
        return  ResponseEntity.ok(userMessages);
    }
    @PostMapping("/createUserMessenger")
    public ResponseEntity<?> createUserMessenger(Authentication authentication , @RequestBody UserProfileDto profile){
        UserProfile sender = profileService.getUserProfile((UserDto) authentication.getPrincipal());
        UserProfile receiver = profileService.getUserProfile(profile.getUser());
        MessengerDto userMessenger = messengerService.createUserMessenger(sender, receiver);
        userMessenger.setImage(profile.getImage());
        return  ResponseEntity.ok(userMessenger);
    }

    @PostMapping("/createGroup")
    public ResponseEntity<?> createGroup(Authentication authentication , @RequestBody UserProfileDto profile){
        UserProfile sender = profileService.getUserProfile((UserDto) authentication.getPrincipal());
        GroupMessenger groupMessenger = messengerService.createGroupMessenger(sender, profile);
        return ResponseEntity.ok(groupMessenger);
    }
    @GetMapping("/userConversation")
    public ResponseEntity<?> startUserConversation(@RequestBody MessengerDto messengerDto,Authentication authentication ){
        UserDto principal = (UserDto) authentication.getPrincipal();
        List<UserMessageDto> userMessages = messageService.getUserMessages(messengerDto);
        return ResponseEntity.ok(userMessages);
    }

    @GetMapping("/groupConversation")
    public ResponseEntity<?> startGroupConversation(@RequestBody MessengerDto messengerDto,Authentication authentication ){
        UserDto principal = (UserDto) authentication.getPrincipal();
        List<GroupMessageDto> groupMessages = messageService.getGroupMessages(messengerDto);
        return ResponseEntity.ok(groupMessages);
    }
}

package com.NewFeed.backend.controller;

import com.NewFeed.backend.dto.*;
import com.NewFeed.backend.modal.messaging.GroupMember;
import com.NewFeed.backend.modal.user.UserProfile;
import com.NewFeed.backend.payload.Request.NewGroupRequest;
import com.NewFeed.backend.payload.Response.GroupMessageResponse;
import com.NewFeed.backend.payload.Response.UpdateMessengerResponse;
import com.NewFeed.backend.payload.Response.UserMessageResponse;
import com.NewFeed.backend.service.UserProfileService;
import com.NewFeed.backend.service.messaging.GroupMemberService;
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
    @Autowired
    private GroupMemberService groupMemberService;

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
        UserMessageResponse userMessageDto = messageService.creatUserMessage(conversationId,profile,message);
        return ResponseEntity.ok(userMessageDto);
    }

    @MessageMapping("/updateTime/{conversationId}")
    @SendTo("/topic/receiveUpdateTime/{conversationId}")
    public ResponseEntity<?> updateTime(Principal principal,
                                         @DestinationVariable Long conversationId,
                                         @RequestBody MessengerDto messengerDto) {
        UserProfile sender = profileService.getUserProfile((UserDto) principal);
        UpdateMessengerResponse messenger = messengerService.updateMessengerTime(sender, messengerDto);
        return ResponseEntity.ok(messenger);
    }

    @MessageMapping("/sendGroupMessage/{conversationId}")
    @SendTo("/topic/GroupConversation/{conversationId}")
    public ResponseEntity<?> sendGroupMessage(Principal principal,
                                         @DestinationVariable Long conversationId,
                                             @RequestBody GroupMessageDto message) {
        UserProfile profile = profileService.getUserProfile((UserDto) principal);
        GroupMember groupMember = groupMemberService.groupMember(conversationId, profile);
        GroupMessageDto groupMessage = messageService.creatGroupMessage(groupMember,message);
        return ResponseEntity.ok(groupMessage);
    }

    @PostMapping("/unreadCount")
    public ResponseEntity<?> unreadCount(Authentication authentication){
        UserProfile profile                = profileService.getUserProfile((UserDto) authentication.getPrincipal());
        return  ResponseEntity.ok(messengerService.unreadMessenger(profile));
    }
    @PostMapping("/getUserMessage")
    public ResponseEntity<?> getUserMessage(Authentication authentication ,@RequestBody MessengerDto messenger){
        UserProfile profile                = profileService.getUserProfile((UserDto) authentication.getPrincipal());
        List<UserMessageDto> userMessages  = messageService.getUserMessages(profile,messenger);
        return  ResponseEntity.ok(userMessages);
    }
    @PostMapping("/getGroupMessage")
    public ResponseEntity<?> getGroupMessage(Authentication authentication ,@RequestBody MessengerDto messenger){
        List<GroupMessageDto> groupMessages = messageService.getGroupMessages(messenger);
        UserProfile profile                = profileService.getUserProfile((UserDto) authentication.getPrincipal());
        GroupMemberDto groupMember         = groupMemberService.getGroupMember(messenger.getConversationId(), profile);
        return  ResponseEntity.ok(GroupMessageResponse
                                      .builder()
                                      .member(groupMember)
                                      .messages(groupMessages)
                                      .build());
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
    public ResponseEntity<?> createGroup(Authentication authentication , @RequestBody NewGroupRequest groupRequest){
        UserProfile sender = profileService.getUserProfile((UserDto) authentication.getPrincipal());
        List<UserProfile> groupMember = groupRequest
                                            .getGroupMember()
                                            .stream()
                                            .map(profile->profileService.getUserProfile(profile.getUser()))
                                            .toList();

        MessengerDto  messenger = messengerService.createGroupMessenger(sender,
                                                                              groupRequest.getMessenger(),
                                                                              groupMember);
        return ResponseEntity.ok(messenger);
    }
    @PostMapping("/updateMessengerTime")
    public ResponseEntity<?> startUserConversation(@RequestBody MessengerDto messengerDto,Authentication authentication ){
        UserProfile sender = profileService.getUserProfile((UserDto) authentication.getPrincipal());
        UpdateMessengerResponse messenger = messengerService.updateMessengerTime(sender, messengerDto);
        return ResponseEntity.ok(messenger);
    }

    @GetMapping("/groupConversation")
    public ResponseEntity<?> startGroupConversation(@RequestBody MessengerDto messengerDto,Authentication authentication ){
        UserDto principal = (UserDto) authentication.getPrincipal();
        List<GroupMessageDto> groupMessages = messageService.getGroupMessages(messengerDto);
        return ResponseEntity.ok(groupMessages);
    }
}

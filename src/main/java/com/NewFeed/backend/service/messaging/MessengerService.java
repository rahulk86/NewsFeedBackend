package com.NewFeed.backend.service.messaging;

import com.NewFeed.backend.dto.MessengerDto;
import com.NewFeed.backend.dto.UserProfileDto;
import com.NewFeed.backend.modal.messaging.GroupMessenger;
import com.NewFeed.backend.modal.user.UserProfile;

import java.util.List;

public interface MessengerService {
    List<MessengerDto> getMessengers(UserProfile user);
    MessengerDto  createUserMessenger( UserProfile sender,UserProfile receiver);
    GroupMessenger createGroupMessenger(UserProfile sender,UserProfileDto profile);

}

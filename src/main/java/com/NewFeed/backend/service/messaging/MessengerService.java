package com.NewFeed.backend.service.messaging;

import com.NewFeed.backend.dto.MessengerDto;
import com.NewFeed.backend.modal.user.UserProfile;
import com.NewFeed.backend.payload.Response.UpdateMessengerResponse;

import java.util.List;

public interface MessengerService {
    List<MessengerDto> getMessengers(UserProfile user);
    MessengerDto  createUserMessenger( UserProfile sender,UserProfile receiver);
    MessengerDto createGroupMessenger(UserProfile sender,MessengerDto messenger,List<UserProfile> receiver) ;
    UpdateMessengerResponse updateMessengerTime(UserProfile profile, MessengerDto messenger);
}

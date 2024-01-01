package com.NewFeed.backend.payload.Response;

import com.NewFeed.backend.dto.MessengerDto;
import com.NewFeed.backend.dto.UserMessageDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserMessageResponse {
    private MessengerDto messenger;
    private UserMessageDto message;
}

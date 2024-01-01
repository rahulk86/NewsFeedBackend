package com.NewFeed.backend.payload.Response;

import com.NewFeed.backend.dto.MessengerDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateMessengerResponse {
    private MessengerDto sender;
    private MessengerDto receiver;

}

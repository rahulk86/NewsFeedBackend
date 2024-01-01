package com.NewFeed.backend.payload.Request;

import com.NewFeed.backend.dto.MessengerDto;
import com.NewFeed.backend.dto.UserProfileDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NewGroupRequest {
    private MessengerDto messenger;
    private List<UserProfileDto> groupMember;
}

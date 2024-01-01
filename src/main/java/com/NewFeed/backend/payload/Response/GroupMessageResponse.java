package com.NewFeed.backend.payload.Response;

import com.NewFeed.backend.dto.GroupMemberDto;
import com.NewFeed.backend.dto.GroupMessageDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class GroupMessageResponse {
    private GroupMemberDto member;
    private List<GroupMessageDto> messages;
}

package com.NewFeed.backend.configuration;

import com.NewFeed.backend.dto.GroupMemberDto;
import com.NewFeed.backend.dto.MessengerDto;
import com.NewFeed.backend.modal.messaging.*;
import com.NewFeed.backend.modal.user.UserProfile;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessengerConfig {
    @Bean
    public ModelMapper messengerConfigModelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        Converter<Conversation, Long > conversationId
                = c -> c. getSource()==null?null:c.getSource().getId();
        Converter<UserProfile, String > userName
                = c -> c. getSource()==null?null:c.getSource().getUser().getName();
        modelMapper.typeMap(UserMessenger.class, MessengerDto.class).addMappings(mapper -> {
            mapper.using(userName).map(UserMessenger::getProfile,MessengerDto::setName);
            mapper.using(conversationId).map(UserMessenger::getConversation,MessengerDto::setConversationId);
        });

        modelMapper.typeMap(GroupMember.class, GroupMemberDto.class).addMappings(mapper -> {
            mapper.using(userName).map(GroupMember::getProfile,GroupMemberDto::setName);
        });

        modelMapper.typeMap(GroupMessenger.class, MessengerDto.class).addMappings(mapper -> {
            mapper.using(conversationId).map(GroupMessenger::getConversation,MessengerDto::setConversationId);
        });

        modelMapper.typeMap(UserProfile.class, UserMessenger.class).addMappings(mapper -> {
            mapper.map(src->1,UserMessenger::setActive);
            mapper.map(src->null,UserMessenger::setId);
        });



        modelMapper.typeMap(UserProfile.class, GroupMember.class).addMappings(mapper -> {
            mapper.map(src->1,GroupMember::setActive);
            mapper.map(src->null,GroupMember::setId);
            mapper.map(src-> GroupRole.ROLE_USER,GroupMember::setRole);
        });

        modelMapper.typeMap(UserProfile.class, UserConversation.class).addMappings(mapper -> {
            mapper.map(src->1,UserConversation::setActive);
            mapper.map(src-> null,UserConversation::setId);
        });

        return modelMapper;
    }
}

package com.NewFeed.backend.configuration;

import com.NewFeed.backend.dto.MessengerDto;
import com.NewFeed.backend.modal.messaging.UserConversation;
import com.NewFeed.backend.modal.messaging.UserMessenger;
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

        Converter<UserConversation, Long > conversationId
                = c -> c. getSource()==null?null:c.getSource().getId();
        Converter<UserProfile, String > userName
                = c -> c. getSource()==null?null:c.getSource().getUser().getName();
        modelMapper.typeMap(UserMessenger.class, MessengerDto.class).addMappings(mapper -> {
            mapper.using(userName).map(UserMessenger::getProfile,MessengerDto::setName);
            mapper.using(conversationId).map(UserMessenger::getConversation,MessengerDto::setConversationId);
        });


        return modelMapper;
    }
}

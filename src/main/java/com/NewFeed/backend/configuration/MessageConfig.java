package com.NewFeed.backend.configuration;

import com.NewFeed.backend.dto.GroupMessageDto;
import com.NewFeed.backend.dto.UserMessageDto;
import com.NewFeed.backend.modal.messaging.GroupMessage;
import com.NewFeed.backend.modal.messaging.UserMessage;
import com.NewFeed.backend.modal.messaging.UserMessenger;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageConfig {

    @Bean
    public ModelMapper messageConfigModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        Converter<UserMessenger, Long > conversationId
                = c -> c. getSource()==null?null:c.getSource().getId();

        modelMapper.typeMap(UserMessage.class, UserMessageDto.class).addMappings(mapper -> {
            mapper.using(conversationId).map(UserMessage::getMessenger,UserMessageDto::setMessengerId);
        });


        return modelMapper;
    }
}

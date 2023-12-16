package com.NewFeed.backend.configuration;

import com.NewFeed.backend.dto.MessengerDto;
import com.NewFeed.backend.modal.messaging.GroupMessenger;
import com.NewFeed.backend.modal.messaging.Messenger;
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
        Converter<Messenger, Long > groupMessengerId
                = c -> c. getSource()==null?null:c.getSource().getMessengerId();
        Converter<Messenger, String > groupMessengerType
                = c -> c. getSource()==null?null:c.getSource().getMessengerType();
        modelMapper.typeMap(GroupMessenger.class,MessengerDto.class).addMappings(mapper->{
            mapper.using(groupMessengerId).map(GroupMessenger::getMessenger,MessengerDto::setId);
            mapper.using(groupMessengerType).map(GroupMessenger::getMessenger,MessengerDto::setType);
        });

        Converter<UserProfile, String > userName
                = c -> c. getSource()==null?null:c.getSource().getUser().getName();
        modelMapper.typeMap(UserMessenger.class, MessengerDto.class).addMappings(mapper -> {
            mapper.using(groupMessengerId).map(UserMessenger::getMessenger,MessengerDto::setId);
            mapper.using(groupMessengerType).map(UserMessenger::getMessenger,MessengerDto::setType);
            mapper.using(userName).map(UserMessenger::getSender,MessengerDto::setName);
        });


        return modelMapper;
    }
}

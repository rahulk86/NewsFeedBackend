package com.NewFeed.backend.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageConfig {
    @Bean
    public ModelMapper messageConfigModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper;
    }
}

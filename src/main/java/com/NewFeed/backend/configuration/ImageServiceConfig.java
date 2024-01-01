package com.NewFeed.backend.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageServiceConfig {

    @Bean
    public ModelMapper userImageServiceModelMapper() {
        return new ModelMapper();
    }
}

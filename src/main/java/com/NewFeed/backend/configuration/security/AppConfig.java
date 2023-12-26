package com.NewFeed.backend.configuration.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
public class AppConfig {

    @Autowired
    AppProperties appProperties;
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }

    @Bean
    public Logger getLogger(){
       return  LoggerFactory.getLogger(OncePerRequestFilter.class);
    }
}

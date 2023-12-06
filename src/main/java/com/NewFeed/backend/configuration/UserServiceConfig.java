package com.NewFeed.backend.configuration;

import com.NewFeed.backend.dto.UserDto;
import com.NewFeed.backend.dto.UserProfileDto;
import com.NewFeed.backend.modal.NewFeedUser;
import com.NewFeed.backend.modal.Role;
import com.NewFeed.backend.modal.UserProfile;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class UserServiceConfig {


    @Bean
    public ModelMapper userServiceModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        Converter<String, String > encodePassword
                 = c -> c. getSource()==null?"":passwordEncoder().encode(c.getSource());
        Converter<Set<Role>, Set<GrantedAuthority>> authorities
                = c -> c. getSource()==null? new HashSet<>()
                                           :c.getSource()
                                            .stream()
                                            .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                                            .collect(Collectors.toSet());

        modelMapper.typeMap(UserDto.class, NewFeedUser.class).addMappings(mapper -> {
            mapper.using(encodePassword).map(UserDto::getPassword,NewFeedUser::setPassword);
            mapper.map(src->1,NewFeedUser::setActive);
            mapper.map(src -> LocalDateTime.now(),NewFeedUser::setCreatAt);
        });

        modelMapper.typeMap(NewFeedUser.class,UserDto.class).addMappings(mapper->{
            mapper.using(authorities).map(NewFeedUser::getRoles,UserDto::setAuthorities);
        });

        modelMapper.typeMap(UserProfileDto.class, UserProfile.class).addMappings(mapper -> {
            mapper.map(src->1,UserProfile::setActive);
            mapper.map(src -> LocalDateTime.now(),UserProfile::setCreatAt);
        });


        return modelMapper;
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}

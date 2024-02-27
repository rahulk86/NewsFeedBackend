package com.NewFeed.backend.configuration;

import com.NewFeed.backend.dto.UserProfileDto;
import com.NewFeed.backend.modal.user.UserProfile;
import com.auth.dto.UserDto;
import com.auth.modal.user.Role;
import com.auth.modal.user.User;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class UserServiceConfig {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Bean
    public ModelMapper userServiceModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        Converter<String, String > encodePassword
                 = c -> c. getSource()==null?"":passwordEncoder.encode(c.getSource());
        Converter<Set<Role>, Set<GrantedAuthority>> authorities
                = c -> c. getSource()==null? new HashSet<>()
                                           :c.getSource()
                                            .stream()
                                            .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                                            .collect(Collectors.toSet());

        modelMapper.typeMap(User.class,UserDto.class).addMappings(mapper->{
            mapper.using(authorities).map(User::getRoles,UserDto::setAuthorities);
        });



        return modelMapper;
    }
}

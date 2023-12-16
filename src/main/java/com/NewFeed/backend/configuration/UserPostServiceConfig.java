package com.NewFeed.backend.configuration;

import com.NewFeed.backend.dto.UserPostDto;
import com.NewFeed.backend.modal.feed.NewFeedComment;
import com.NewFeed.backend.modal.feed.NewFeedPost;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class UserPostServiceConfig {
    @Bean
    public ModelMapper userPostServiceModelMapper() {
       ModelMapper modelMapper = new ModelMapper();
        Converter<List<NewFeedComment>, Long > commentsToLong
                = c -> c. getSource()==null?0L: (long) c.getSource().size();


        modelMapper.typeMap(NewFeedPost.class, UserPostDto.class).addMappings(mapper -> {
            mapper.using(commentsToLong).map(NewFeedPost::getComments,UserPostDto::setComments);
        });


        modelMapper.typeMap(UserPostDto.class, NewFeedPost.class).addMappings(mapper -> {
            mapper.map(src->1,NewFeedPost::setActive);
            mapper.map(src -> LocalDateTime.now(),NewFeedPost::setCreatAt);
        });
        return modelMapper;
    }
}

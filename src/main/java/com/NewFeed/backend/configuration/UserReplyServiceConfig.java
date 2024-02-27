package com.NewFeed.backend.configuration;

import com.NewFeed.backend.dto.UserReplyDto;
import com.NewFeed.backend.modal.feed.NewFeedReply;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class UserReplyServiceConfig {
    @Bean
    public ModelMapper UserReplyServiceModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        Converter<List<NewFeedReply>, Long> repliesToLong = c -> Long.valueOf(c.getSource()==null?0:c.getSource().size());
        modelMapper.typeMap(NewFeedReply.class, UserReplyDto.class).addMappings(mapper -> {
            mapper.map(src -> src.getParent().getId(),UserReplyDto::setCommentId);
            mapper.map(src -> src.getParent().getId(),UserReplyDto::setReplyId);
            mapper.using(repliesToLong).map(NewFeedReply::getReplies,UserReplyDto::setReplies);
        });
        return modelMapper;
    }
}

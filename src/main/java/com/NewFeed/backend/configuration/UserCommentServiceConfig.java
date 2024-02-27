package com.NewFeed.backend.configuration;

import com.NewFeed.backend.dto.UserCommentDto;
import com.NewFeed.backend.modal.feed.NewFeedComment;
import com.NewFeed.backend.modal.feed.NewFeedReply;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class UserCommentServiceConfig {
    @Bean
    public ModelMapper userCommentServiceModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        Converter<List<NewFeedReply>, Long> repliesToLong = c -> Long.valueOf(c.getSource()==null?0:c.getSource().size());
        modelMapper.typeMap(NewFeedComment.class, UserCommentDto.class).addMappings(mapper -> {
            mapper.map(src -> src.getParent().getId(),UserCommentDto::setPostId);
            mapper.using(repliesToLong).map(NewFeedComment::getCommentReplies,UserCommentDto::setReplies);
        });

        return modelMapper;
    }
}

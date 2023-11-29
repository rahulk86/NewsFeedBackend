package com.NewFeed.backend.service;

import com.NewFeed.backend.dto.UserCommentDto;
import com.NewFeed.backend.dto.UserPostDto;
import com.NewFeed.backend.modal.NewFeedPost;

import java.io.IOException;
import java.util.List;

public interface UserPostService {
     NewFeedPost createPost(Long userId, UserPostDto userPostDto) throws IOException;
     List<UserPostDto> getPosts(Long id);
     Object[] getPost(Long postId, Long userId);
     void validateComment(UserCommentDto userCommentDto);
     UserPostDto postToDto(Object[] post);
}

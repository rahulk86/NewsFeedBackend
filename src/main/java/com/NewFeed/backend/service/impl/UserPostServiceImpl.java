package com.NewFeed.backend.service.impl;


import com.NewFeed.backend.dto.ImageDto;
import com.NewFeed.backend.dto.UserCommentDto;
import com.NewFeed.backend.dto.UserPostDto;
import com.NewFeed.backend.dto.UserProfileDto;
import com.NewFeed.backend.exception.UserPostException;
import com.NewFeed.backend.modal.image.Image;
import com.NewFeed.backend.modal.feed.NewFeedPost;
import com.NewFeed.backend.modal.user.UserProfile;
import com.NewFeed.backend.repository.image.ImageRepository;
import com.NewFeed.backend.repository.user.UserProfileRepository;
import com.NewFeed.backend.repository.feed.UserCommentRepository;
import com.NewFeed.backend.repository.feed.UserPostRepository;
import com.NewFeed.backend.repository.feed.VoteRepository;
import com.NewFeed.backend.service.UserPostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserPostServiceImpl implements UserPostService {
    @Autowired
    private UserPostRepository userPostRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private UserCommentRepository userCommentRepository;
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    @Qualifier("userPostServiceModelMapper")
    private  ModelMapper postModelMapper;

    @Autowired
    @Qualifier("userImageServiceModelMapper")
    private  ModelMapper imageModelMapper;

    @Autowired
    @Qualifier("userServiceModelMapper")
    private  ModelMapper userModelMapper;

    @Override
    @Transactional
    public NewFeedPost createPost(Long userId, UserPostDto userPostDto) throws IOException {
        NewFeedPost newFeedPost = this.postModelMapper.map(userPostDto, NewFeedPost.class);
        UserProfile userProfile = userProfileRepository.
                                    findByUserId(userId).
                                    orElseThrow(() -> new UserPostException("UserPostException !! User is not exists with given id :" + userId));
        newFeedPost.setUserProfile(userProfile);
        return  userPostRepository.save(newFeedPost);

    }


    @Override
    @Transactional
    public List<UserPostDto> getPosts(Long id) {
        List<Object[]> byUserId = userPostRepository.
                findByUserId(id).
                orElseThrow(() -> new UserPostException("UserPostException !! User is not exists with given id :" + id));
        return byUserId
                .stream()
                .map(this::postToDto)
                .collect(Collectors.toList());

    }

    @Override
    @Transactional
    public void validateComment(UserCommentDto userCommentDto) {
        NewFeedPost newFeedPost = userPostRepository.
                                    findById(userCommentDto.getPostId()).
                                    orElse(null);
        if(newFeedPost==null){
            userCommentDto.setPostId((long) -1);
        }

    }

    @Override
    @Transactional
    public UserPostDto postToDto(Object[] post) {
        int index = 0;
        if(post[0] instanceof NewFeedPost feedPost){
            UserPostDto userPostDto = this.postModelMapper.map(feedPost, UserPostDto.class);
            userPostDto.setUserProfile(userModelMapper.map(userPostDto.getUserProfile(), UserProfileDto.class));
            if(post[1] instanceof Image) {
               userPostDto.setImage(this.imageModelMapper.map((Image)post[1], ImageDto.class));
            }
            userPostDto.setUpVote((Long) post[2]);
            userPostDto.setDownVote((Long) post[3]);
            userPostDto.setLiked((Boolean) post[4]);
            userPostDto.setDisLiked((Boolean) post[5]);
            if(post[6] instanceof Image){
                userPostDto.getUserProfile().setImage(this.imageModelMapper.map((Image) post[index], ImageDto.class));
            }

            return userPostDto;
        }
        return null;
    }

    @Override
    @Transactional
    public Object[] getPost(Long postId, Long userId) {
        Object[] objects = userPostRepository.
                findById(postId, userId).
                orElseThrow(() -> new UserPostException("UserPostException !! post is not exists with given post id :" + postId));
        return (Object[]) objects[0];
    }


}

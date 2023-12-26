package com.NewFeed.backend.service.impl;

import com.NewFeed.backend.dto.*;
import com.NewFeed.backend.exception.UserCommentException;
import com.NewFeed.backend.modal.feed.NewFeedComment;
import com.NewFeed.backend.modal.feed.NewFeedPost;
import com.NewFeed.backend.modal.feed.Votable;
import com.NewFeed.backend.modal.image.Image;
import com.NewFeed.backend.modal.user.UserProfile;
import com.NewFeed.backend.payload.Request.VoteOnComments;
import com.NewFeed.backend.repository.feed.UserCommentRepository;
import com.NewFeed.backend.repository.feed.UserPostRepository;
import com.NewFeed.backend.repository.user.UserProfileRepository;
import com.NewFeed.backend.service.UserCommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserCommentServiceImpl implements UserCommentService {
    @Autowired
    UserCommentRepository userCommentRepository;
    @Autowired
    private UserPostRepository userPostRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    @Qualifier("userServiceModelMapper")
    private  ModelMapper userModelMapper;
    @Autowired
    @Qualifier("userCommentServiceModelMapper")
    private  ModelMapper commentModelMapper;

    @Autowired
    @Qualifier("userImageServiceModelMapper")
    private  ModelMapper imageModelMapper;
    @Override
    public UserCommentDto createComment(UserDto userDto,UserCommentDto userCommentDto) {
        NewFeedComment newFeedComment = this.commentModelMapper.map(userCommentDto, NewFeedComment.class);
        userDto.setPassword("");
        UserProfile userProfile = userProfileRepository.
                findByUserId(userDto.getId()).
                orElseThrow(() -> new UserCommentException("UserCommentException !! User is not exists with given id :" + userDto.getId()));

        NewFeedPost newFeedPost = userPostRepository.findById(userCommentDto.getPostId()).
                orElseThrow(() -> new UserCommentException("UserCommentException !! not allow to comment on this post id :" + userCommentDto.getPostId()));;
        newFeedComment.setParent(newFeedPost);
        newFeedComment.setUserProfile(userProfile);

        return this.commentModelMapper.map(userCommentRepository.save(newFeedComment),UserCommentDto.class);
    }

    @Override
    public List<UserCommentDto> getCommentByPostId(Long userId, Long postId) {
        UserProfile userProfile = userProfileRepository.
                findByUserId(userId).
                orElseThrow(() -> new UserCommentException("UserCommentException !! User is not exists with given id :" + userId));
        NewFeedPost newFeedPost = userPostRepository.findById(postId).
                orElseThrow(() -> new UserCommentException("UserCommentException !! not allow to comment on this post id :" + postId));;


        return  userCommentRepository
                    .findAllByPostAndUserId(newFeedPost,userId)
                    .orElse(new ArrayList<>())
                    .stream()
                    .map(this::commentToDto)
                    .collect(Collectors.toList());
    }

    public UserCommentDto commentToDto(Object[] post) {
        int index = 0;
        if(post[index++] instanceof NewFeedComment comment){
            UserCommentDto userCommentDto = this.commentModelMapper.map(comment, UserCommentDto.class);
            userCommentDto.setUserProfile(userModelMapper.map(userCommentDto.getUserProfile(), UserProfileDto.class));
            userCommentDto.setUpVotes((Long) post[index++]);
            userCommentDto.setDownVotes((Long) post[index++]);
            userCommentDto.setLiked((Boolean) post[index++]);
            userCommentDto.setDisLiked((Boolean) post[index++]);
            if(post[index] instanceof Image){
                userCommentDto.getUserProfile().setImage(this.imageModelMapper.map((Image) post[index], ImageDto.class));
            }

            return userCommentDto;
        }
        return null;
    }

    @Override
    public Votable getVotable(VoteOnComments voteOnComments) {
               userPostRepository.
                findById(voteOnComments.getPostId()).
                orElseThrow(()->new UserCommentException("UserCommentException !! post is not exists with given post id :" + voteOnComments.getPostId()));
        return userCommentRepository.
                findById(voteOnComments.getCommentId()).
                orElseThrow(() -> new UserCommentException("UserCommentException !! comment is not exists with given comment id :" + voteOnComments.getCommentId()));
    }
    @Override
    public UserCommentDto votableToDto(Votable votable){
        if(votable instanceof NewFeedComment){
            return  this.commentModelMapper.map((NewFeedComment)votable, UserCommentDto.class);
        }
        return null;
    }

    @Override
    public void validateComment(UserReplyDto userReplyDto) {
        NewFeedComment comment = userCommentRepository.
                findById(userReplyDto.getCommentId()).
                orElse(null);
        if(comment==null){
            userReplyDto.setCommentId((long) -1);
        }

    }
}

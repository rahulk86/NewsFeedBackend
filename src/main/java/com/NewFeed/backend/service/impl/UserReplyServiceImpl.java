package com.NewFeed.backend.service.impl;

import com.NewFeed.backend.configuration.security.AppProperties;
import com.NewFeed.backend.dto.*;
import com.NewFeed.backend.exception.UserReplyException;
import com.NewFeed.backend.modal.feed.NewFeedComment;
import com.NewFeed.backend.modal.feed.NewFeedReply;
import com.NewFeed.backend.modal.feed.Votable;
import com.NewFeed.backend.modal.image.Image;
import com.NewFeed.backend.modal.user.UserProfile;
import com.NewFeed.backend.payload.Request.VoteOnReply;
import com.NewFeed.backend.repository.feed.UserCommentRepository;
import com.NewFeed.backend.repository.feed.UserPostRepository;
import com.NewFeed.backend.repository.user.UserProfileRepository;
import com.NewFeed.backend.repository.feed.UserReplyRepository;
import com.NewFeed.backend.service.UserReplyService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserReplyServiceImpl implements UserReplyService {
    @Autowired
    private AppProperties appProperties;
    @Autowired
    private UserCommentRepository userCommentRepository;
    @Autowired
    private UserReplyRepository userReplyRepository;
    @Autowired
    private UserPostRepository userPostRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    @Qualifier("userServiceModelMapper")
    private  ModelMapper userModelMapper;

    @Autowired
    @Qualifier("UserReplyServiceModelMapper")
    private  ModelMapper replyServiceModelMapper;
    @Autowired
    @Qualifier("userCommentServiceModelMapper")
    private  ModelMapper commentModelMapper;

    @Autowired
    @Qualifier("userImageServiceModelMapper")
    private  ModelMapper imageModelMapper;

    @Override
    public UserReplyDto createCommentReply(UserDto userDto, UserReplyDto userReplyDto) {
        NewFeedReply reply = replyServiceModelMapper.map(userReplyDto, NewFeedReply.class);
        UserProfile userProfile = userProfileRepository.
                findByUserId(userDto.getId()).
                orElseThrow(() -> new UserReplyException("UserReplyException !! User is not exists with given email :" + userDto.getEmail()));

        NewFeedComment comment = userCommentRepository.
                findById(userReplyDto.getCommentId()).
                orElseThrow(()->new UserReplyException("UserReplyException !! comment is not exists with given comment id :" + userReplyDto.getCommentId()));
        reply.setParent(comment);
        reply.setUserProfile(userProfile);
        reply.setCreatAt(appProperties.now());
        return replyServiceModelMapper.map(userReplyRepository.save(reply),UserReplyDto.class);
    }

    @Override
    public UserReplyDto createReplyReply(UserDto userDto, UserReplyDto userReplyDto) {
        NewFeedReply reply = replyServiceModelMapper.map(userReplyDto, NewFeedReply.class);
        UserProfile userProfile = userProfileRepository.
                findByUserId(userDto.getId()).
                orElseThrow(() -> new UserReplyException("UserReplyException !! User is not exists with given email :" + userDto.getEmail()));

        NewFeedReply reply1 = userReplyRepository.
                findById(userReplyDto.getReplyId()).
                orElseThrow(()->new UserReplyException("UserReplyException !! reply is not exists with given reply id :" + userReplyDto.getReplyId()));
        reply.setParent(reply1);
        reply.setCreatAt(appProperties.now());
        reply.setUserProfile(userProfile);
        return replyServiceModelMapper.map(userReplyRepository.save(reply),UserReplyDto.class);

    }

    @Override
    public List<UserReplyDto> getReplyByCommentId(Long commentId,Long userId) {
        UserProfile userProfile = userProfileRepository.
                findByUserId(userId).
                orElseThrow(() -> new UserReplyException("UserReplyException !! User is not exists with given id :" + userId));
        NewFeedComment comment = userCommentRepository.findById(commentId).
                orElseThrow(() -> new UserReplyException("UserReplyException !! not allow to reply on this comment id :" + commentId));;


        return  userReplyRepository
                .findAllByReplyableAndUserId(comment,userId)
                .orElse(new ArrayList<>())
                .stream()
                .map(this::replyToDto)
                .collect(Collectors.toList());
    }

    public UserReplyDto replyToDto(Object[] post) {
        int index = 0;
        if(post[index++] instanceof NewFeedReply reply){
            UserReplyDto userReplyDto = this.replyServiceModelMapper.map(reply, UserReplyDto.class);
            userReplyDto.setUserProfile(userModelMapper.map(userReplyDto.getUserProfile(), UserProfileDto.class));
            userReplyDto.setUpVotes((Long) post[index++]);
            userReplyDto.setDownVotes((Long) post[index++]);
            userReplyDto.setLiked((Boolean) post[index++]);
            userReplyDto.setDisLiked((Boolean) post[index++]);
            if(post[index] instanceof Image){
                userReplyDto.getUserProfile().setImage(this.imageModelMapper.map((Image) post[index], ImageDto.class));
            }

            return userReplyDto;
        }
        return null;
    }
    @Override
    public List<UserReplyDto> getReplyByReplyId(Long replyId,Long userId) {
        UserProfile userProfile = userProfileRepository.
                findByUserId(userId).
                orElseThrow(() -> new UserReplyException("UserReplyException !! User is not exists with given id :" + userId));
        NewFeedReply reply = userReplyRepository.findById(replyId).
                orElseThrow(() -> new UserReplyException("UserReplyException !! not allow to reply on this reply id :" + replyId));;


        return  userReplyRepository
                .findAllByReplyableAndUserId(reply,userId)
                .orElse(new ArrayList<>())
                .stream()
                .map(this::replyToDto)
                .collect(Collectors.toList());
    }
    @Override
    public Votable getVotableOnComment(VoteOnReply voteOnReply) {
            userCommentRepository.
                findById(voteOnReply.getCommentId()).
                orElseThrow(() -> new UserReplyException("UserReplyException !! comment is not exists with given comment id :" + voteOnReply.getCommentId()));

        return userReplyRepository.
                findById(voteOnReply.getReplyId()).
                orElseThrow(() -> new UserReplyException("UserReplyException !! reply is not exists with given reply id :" + voteOnReply.getReplyId()));
    }

    @Override
    public void validateReply(UserReplyDto userReplyDto) {
        NewFeedReply reply = userReplyRepository.
                findById(userReplyDto.getReplyId()).
                orElse(null);
        if(reply==null){
            userReplyDto.setReplyId((long) -1);
        }

    }

    @Override
    public Votable getVotableOnReply(VoteOnReply voteOnReply) {
        return userReplyRepository.findById(voteOnReply.
                        getReplyId()).
                orElseThrow(() -> new UserReplyException("UserReplyException !! reply is not exists with given reply id :" + voteOnReply.getReplyId()));
    }

    @Override
    public UserReplyDto votableToDto(Votable votable) {
        if(votable instanceof NewFeedReply){
            return  this.replyServiceModelMapper.map((NewFeedReply)votable, UserReplyDto.class);
        }
        return null;
    }
}

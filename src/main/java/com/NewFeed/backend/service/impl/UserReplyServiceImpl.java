package com.NewFeed.backend.service.impl;

import com.NewFeed.backend.dto.UserDto;
import com.NewFeed.backend.dto.UserReplyDto;
import com.NewFeed.backend.exception.UserReplyException;
import com.NewFeed.backend.modal.*;
import com.NewFeed.backend.payload.Request.VoteOnReply;
import com.NewFeed.backend.repository.*;
import com.NewFeed.backend.service.UserReplyService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserReplyServiceImpl implements UserReplyService {
    @Autowired
    UserCommentRepository userCommentRepository;
    @Autowired
    UserReplyRepository userReplyRepository;
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

    @Override
    public UserReplyDto createCommentReply(UserDto userDto, UserReplyDto userReplyDto) {
        NewFeedReply reply = replyServiceModelMapper.map(userReplyDto, NewFeedReply.class);
        UserProfile userProfile = userProfileRepository.
                findByUserId(userDto.getId()).
                orElseThrow(() -> new UserReplyException("UserReplyException !! User is not exists with given email :" + userDto.getEmail()));

        NewFeedComment comment = userCommentRepository.
                findById(userReplyDto.getCommentId()).
                orElseThrow(()->new UserReplyException("UserReplyException !! comment is not exists with given comment id :" + userReplyDto.getCommentId()));
        reply.setComment(comment);
        reply.setUserProfile(userProfile);
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
        reply.setReply(reply1);
        reply.setUserProfile(userProfile);
        reply.setComment(reply1.getComment());
        return replyServiceModelMapper.map(userReplyRepository.save(reply),UserReplyDto.class);

    }

    @Override
    public List<UserReplyDto> getReplyByCommentId(Long id) {
        return null;
    }

    @Override
    public List<UserReplyDto> getReplyByReplyId(Long id) {
        return null;
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

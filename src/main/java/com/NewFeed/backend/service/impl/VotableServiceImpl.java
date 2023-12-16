package com.NewFeed.backend.service.impl;

import com.NewFeed.backend.exception.VoteException;
import com.NewFeed.backend.modal.user.NewFeedUser;
import com.NewFeed.backend.modal.feed.Votable;
import com.NewFeed.backend.modal.feed.Vote;
import com.NewFeed.backend.modal.feed.VoteType;
import com.NewFeed.backend.repository.user.UserRepository;
import com.NewFeed.backend.repository.user.feed.VoteRepository;
import com.NewFeed.backend.service.VotableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VotableServiceImpl implements VotableService {
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    UserRepository userRepository;
    @Override
    public void upVote(Long userId, Votable votable) {
        NewFeedUser newFeedUser = userRepository.
                findById(userId).
                orElseThrow(() -> new VoteException("VoteException !! User is not exists with given id :" + userId));

        deactivateVote(newFeedUser,votable,VoteType.DOWNVOTE);
        vote(newFeedUser,votable,VoteType.UPVOTE);
    }
    private void vote(NewFeedUser user, Votable votable,VoteType voteType){
        Vote vote = voteRepository.findByUserAndVotableTypeAndVotableIdAndVoteType(user,
                        votable.getClass().getSimpleName(),
                        votable.getId(),
                        voteType
                ).orElse(null);
        if(vote==null || vote.getActive()==0){
            Vote newVote = vote==null?new Vote():vote;
            newVote.setActive(1);
            newVote.setUser(user);
            newVote.setCreatAt(LocalDateTime.now());
            newVote.setVotableType(votable.getClass().getSimpleName());
            newVote.setVotableId(votable.getId());
            newVote.setVoteType(voteType);
            voteRepository.save(newVote);
//            votable.vote(voteType);
        }
    }
    private void deactivateVote(NewFeedUser user, Votable votable, VoteType voteType){
        Vote vote = voteRepository.findByUserAndVotableTypeAndVotableIdAndVoteType( user,
                        votable.getClass().getSimpleName(),
                        votable.getId(),
                        voteType
                ).orElse(null);
        if(vote!=null && vote.getActive()!=0){
            vote.setActive(0);
            vote.setCreatAt(LocalDateTime.now());
            voteRepository.save(vote);
//            votable.unVote(voteType);
        }
    }
    @Override
    public void  downVote(Long userId, Votable votable) {
        NewFeedUser newFeedUser = userRepository.
                findById(userId).
                orElseThrow(() -> new VoteException("VoteException !! User is not exists with given id :" + userId));

        deactivateVote(newFeedUser,votable,VoteType.UPVOTE);
        vote(newFeedUser,votable,VoteType.DOWNVOTE);
    }
}

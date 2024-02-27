package com.NewFeed.backend.service.impl;

import com.NewFeed.backend.exception.VoteException;
import com.NewFeed.backend.modal.feed.Votable;
import com.NewFeed.backend.modal.feed.Vote;
import com.NewFeed.backend.modal.feed.VoteType;
import com.NewFeed.backend.repository.feed.VoteRepository;
import com.NewFeed.backend.service.VotableService;
import com.auth.modal.user.User;
import com.auth.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VotableServiceImpl implements VotableService {
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    UserRepository userRepository;
    @Transactional
    @Override
    public void upVote(Long userId, Votable votable) {
        User user = userRepository.
                findById(userId).
                orElseThrow(() -> new VoteException("VoteException !! User is not exists with given id :" + userId));

        deactivateVote(user,votable,VoteType.DOWNVOTE);
        vote(user,votable,VoteType.UPVOTE);
    }
    private void vote(User user, Votable votable, VoteType voteType){
        Vote vote = voteRepository.findByUserAndVotableTypeAndVotableIdAndVoteType(user,
                        votable.getClass().getSimpleName(),
                        votable.getId(),
                        voteType
                ).orElse(null);
        if(vote==null){
            Vote newVote = vote==null?new Vote():vote;
            newVote.setUser(user);
            newVote.setVotableType(votable.getClass().getSimpleName());
            newVote.setVotableId(votable.getId());
            newVote.setVoteType(voteType);
            voteRepository.save(newVote);
//            votable.vote(voteType);
        }
    }
    private void deactivateVote(User user, Votable votable, VoteType voteType){
        Vote vote = voteRepository.findByUserAndVotableTypeAndVotableIdAndVoteType( user,
                        votable.getClass().getSimpleName(),
                        votable.getId(),
                        voteType
                ).orElse(null);
        if(vote!=null ){
            voteRepository.save(vote);
//            votable.unVote(voteType);
        }
    }
    @Override
    @Transactional
    public void  downVote(Long userId, Votable votable) {
        User user = userRepository.
                findById(userId).
                orElseThrow(() -> new VoteException("VoteException !! User is not exists with given id :" + userId));

        deactivateVote(user,votable,VoteType.UPVOTE);
        vote(user,votable,VoteType.DOWNVOTE);
    }
}

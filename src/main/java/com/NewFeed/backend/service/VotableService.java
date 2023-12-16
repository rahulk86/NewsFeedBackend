package com.NewFeed.backend.service;

import com.NewFeed.backend.modal.feed.Votable;

public interface VotableService {
   void upVote(Long userId, Votable votable);
    void downVote(Long userId, Votable votable);
}

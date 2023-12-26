package com.NewFeed.backend.repository.feed;

import com.NewFeed.backend.modal.user.NewFeedUser;
import com.NewFeed.backend.modal.feed.Vote;
import com.NewFeed.backend.modal.feed.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<List<Vote>> findByVotableTypeAndVotableIdAndVoteType(String votableType,
                                                                  Long votableId,
                                                                  VoteType voteType);
    Optional<Vote> findByUserAndVotableTypeAndVotableIdAndVoteType(NewFeedUser user,
                                                                   String votableType,
                                                                   Long votableId,
                                                                   VoteType voteType);
}

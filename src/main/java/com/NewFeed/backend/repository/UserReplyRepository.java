package com.NewFeed.backend.repository;

import com.NewFeed.backend.modal.NewFeedReply;
import com.NewFeed.backend.modal.NewFeedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserReplyRepository extends JpaRepository<NewFeedReply,Long> {
    Optional<List<NewFeedReply>> findByUserProfileAndCommentId(NewFeedUser user,Long commentId);
    Optional<List<NewFeedReply>> findByUserProfileAndReplyId(NewFeedUser user,Long ReplyId);

    @Query("select new com.NewFeed.backend.modal.NewFeedReply( reply ," +
            " count(upVote) ," +
            " count(downVote) " +
            ") " +
            "from NewFeedReply reply " +
            "left join Vote upVote on upVote.votableId = reply.id and" +
            " upVote.votableType = 'NewFeedReply' and" +
            " upVote.voteType = VoteType.UPVOTE and" +
            " upVote.active = 1 " +
            "left join Vote downVote on downVote.votableId = reply.id and" +
            " downVote.votableType = 'NewFeedReply' and" +
            " downVote.voteType = VoteType.DOWNVOTE and" +
            " downVote.active = 1 " +
            "where reply.id = ?1 ")
    @Override
    Optional<NewFeedReply> findById(Long replyId);
}

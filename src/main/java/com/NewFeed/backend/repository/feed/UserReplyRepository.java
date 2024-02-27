package com.NewFeed.backend.repository.feed;

import com.NewFeed.backend.modal.feed.NewFeedReply;
import com.NewFeed.backend.modal.feed.Replyable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserReplyRepository extends JpaRepository<NewFeedReply,Long> {
    @Query("select " +
                " reply ," +
                " count(upVote) ," +
                " count(downVote) ," +
                " SUM(CASE WHEN upVote.user.id = ?2 THEN 1 ELSE 0 END) > 0 ,"+
                " SUM(CASE WHEN downVote.user.id = ?2 THEN 1 ELSE 0 END) > 0 , "+
                " profileImage "+
            "from NewFeedReply reply " +
                "left join Image profileImage on profileImage.imageableId = reply.userProfile.id and" +
                    " profileImage.imageableType = 'UserProfile' " +
                "left join Vote upVote on upVote.votableId = reply.id and" +
                    " upVote.votableType = 'NewFeedComment' and" +
                    " upVote.voteType = VoteType.UPVOTE " +
                "left join Vote downVote on downVote.votableId = reply.id and" +
                    " downVote.votableType = 'NewFeedComment' and" +
                    " downVote.voteType = VoteType.DOWNVOTE  " +
                "where reply.parent = ?1 "+
                "group by " +
                    "reply , profileImage ")
    Optional<List<Object[]>> findAllByReplyableAndUserId(Replyable readable, Long userId);
}

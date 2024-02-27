package com.NewFeed.backend.repository.feed;

import com.NewFeed.backend.modal.feed.NewFeedComment;
import com.NewFeed.backend.modal.feed.NewFeedPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserCommentRepository extends JpaRepository<NewFeedComment,Long> {
    Optional<List<NewFeedComment>> findByParent(NewFeedPost post);

    @Query("select " +
                " comment ," +
                " count(upVote) ," +
                " count(downVote) ," +
                " SUM(CASE WHEN upVote.user.id = ?2 THEN 1 ELSE 0 END) > 0 ,"+
                " SUM(CASE WHEN downVote.user.id = ?2 THEN 1 ELSE 0 END) > 0 , "+
                " profileImage "+
            "from NewFeedComment comment " +
                "left join Image profileImage on profileImage.imageableId = comment.userProfile.id and" +
                    " profileImage.imageableType = 'UserProfile' " +
                "left join Vote upVote on upVote.votableId = comment.id and" +
                    " upVote.votableType = 'NewFeedComment' and" +
                    " upVote.voteType = VoteType.UPVOTE " +
                "left join Vote downVote on downVote.votableId = comment.id and" +
                    " downVote.votableType = 'NewFeedComment' and" +
                    " downVote.voteType = VoteType.DOWNVOTE " +
                "where comment.parent = ?1 "+
                "group by " +
                    "comment , profileImage ")
    Optional<List<Object[]>> findAllByPostAndUserId(NewFeedPost post, Long userId);
}

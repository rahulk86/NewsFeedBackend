package com.NewFeed.backend.repository;

import com.NewFeed.backend.modal.NewFeedPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserPostRepository extends JpaRepository<NewFeedPost,Long> {
    @Query("select " +
                " post ," +
                " image ,"+
                " count(upVote) ," +
                " count(downVote) ," +
                " SUM(CASE WHEN upVote.user.id = ?1 THEN 1 ELSE 0 END) > 0 ,"+
                " SUM(CASE WHEN downVote.user.id = ?1 THEN 1 ELSE 0 END) > 0 , "+
                " profileImage "+
            "from feedPost post " +
                "left join Image image on image.imageableId = post.id and" +
                    " image.imageableType = 'NewFeedPost' and" +
                    " image.active = 1 " +
                "left join Image profileImage on profileImage.imageableId = post.userProfile.id and" +
                    " profileImage.imageableType = 'UserProfile' and" +
                    " profileImage.active = 1 " +
                "left join Vote upVote on upVote.votableId = post.id and" +
                    " upVote.votableType = 'NewFeedPost' and" +
                    " upVote.voteType = VoteType.UPVOTE and" +
                    " upVote.active = 1 " +
                "left join Vote downVote on downVote.votableId = post.id and" +
                    " downVote.votableType = 'NewFeedPost' and" +
                    " downVote.voteType = VoteType.DOWNVOTE and" +
                    " downVote.active = 1 " +
                "left join Followed followed on followed.followedUser = post.userProfile.user and" +
                    " followed.user.id = ?1  and" +
                    " followed.active = 1 " +
                "group by " +
                      "post , image , profileImage "+
                    "order by MAX(followed) DESC," +
                    "count(upVote) - count(downVote) DESC, " +
                    "size(post.comments) DESC, " +
                    "post.creatAt DESC ")
    Optional<List<Object[]>> findByUserId(Long userId);
    @Query("select " +
                " post ," +
                " image ,"+
                " count(upVote) ," +
                " count(downVote) ," +
                " SUM(CASE WHEN upVote.user.id = ?2 THEN 1 ELSE 0 END) > 0 ,"+
                " SUM(CASE WHEN downVote.user.id = ?2 THEN 1 ELSE 0 END) > 0 ,"+
                " profileImage "+
            "from feedPost post " +
                "left join Image image on image.imageableId = post.id and" +
                    " image.imageableType = 'NewFeedPost' and" +
                    " image.active = 1 " +
                "left join Image profileImage on profileImage.imageableId = post.userProfile.id and" +
                    " profileImage.imageableType = 'UserProfile' and" +
                    " profileImage.active = 1 " +
                "left join Vote upVote on upVote.votableId = post.id and" +
                    " upVote.votableType = 'NewFeedPost' and" +
                    " upVote.voteType = VoteType.UPVOTE and" +
                    " upVote.active = 1 " +
                "left join Vote downVote on downVote.votableId = post.id and" +
                    " downVote.votableType = 'NewFeedPost' and" +
                    " downVote.voteType = VoteType.DOWNVOTE and" +
                    " downVote.active = 1 " +
                "where post.id = ?1 " +
                "group by " +
                    "post , image , profileImage ")
    Optional<Object[]> findById(Long postId,Long userId);
}

package com.NewFeed.backend.modal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "feedReply")
public class NewFeedReply extends NewFeedTextContent implements Votable {
    @ManyToOne
    private NewFeedComment comment;
    @ManyToOne
    private NewFeedReply reply;
    @Transient
    private Long upVotes;
    @Transient
    private boolean liked;
    @Transient
    private Long downVotes;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reply")
    private List<NewFeedReply> replies;

    public NewFeedReply(NewFeedReply NewFeedReply ,long upvVotes,long downVotes){
        this.setComment(NewFeedReply.getComment());
        this.setReply(NewFeedReply.getReply());
        this.setText(NewFeedReply.getText());
        this.setId(NewFeedReply.getId());
        this.setActive(NewFeedReply.getActive());
        this.setUpVotes(upvVotes);
        this.setDownVotes(downVotes);
        this.setReplies(NewFeedReply.getReplies());
        this.setCreatAt(NewFeedReply.getCreatAt());
        this.setUserProfile(NewFeedReply.getUserProfile());
    }
//    @Override
//    public VotableType getType() {
//        return VotableType.REPLY;
//    }

//    @Override
//    public void vote(VoteType voteType) {
//        if(voteType==VoteType.UPVOTE) {
//            upVotes = upVotes == null ? 1L : upVotes + 1L;
//        }
//        else{
//            downVotes = downVotes == null ? 1L : downVotes + 1L;
//        }
//    }
//
//    @Override
//    public void unVote(VoteType voteType) {
//        if(voteType==VoteType.UPVOTE) {
//            upVotes = upVotes == null || upVotes==0L ? 0L : upVotes -1L;
//        }
//        else{
//            downVotes = downVotes == null || downVotes==0L ? 0L : downVotes -1L;
//        }
//    }
}

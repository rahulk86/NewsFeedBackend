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
public class NewFeedComment extends Replyable implements Votable{
    @ManyToOne
    private NewFeedPost parent;
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "parent")
    private List<NewFeedReply> commentReplies;


}

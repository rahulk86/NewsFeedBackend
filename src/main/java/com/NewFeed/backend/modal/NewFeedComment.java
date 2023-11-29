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
@Table(name = "feedComment")
public class NewFeedComment extends NewFeedTextContent implements Votable{
    @ManyToOne
    private NewFeedPost post;
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "comment")
    private List<NewFeedReply> commentReplies;


}

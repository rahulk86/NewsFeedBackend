package com.NewFeed.backend.payload.Request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentsByPost {
    @NotNull
    private Long postId;
}

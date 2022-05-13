package com.harrydrummond.projecthjd.posts.comment.dto;

import lombok.Data;

@Data
public class CommentRequestDTO {
    private final String comment;
    private Long parentCommentId;
}
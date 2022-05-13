package com.harrydrummond.projecthjd.posts.comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    Optional<Comment> getCommentById(long id);

    Comment saveComment(Comment comment);

    List<Comment> getAllComments();

    Comment updateComment(Comment comment);

    void deleteComment(long id);
}
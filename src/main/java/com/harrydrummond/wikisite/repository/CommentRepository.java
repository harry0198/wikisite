package com.harrydrummond.wikisite.repository;

import com.harrydrummond.wikisite.entity.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> {
}
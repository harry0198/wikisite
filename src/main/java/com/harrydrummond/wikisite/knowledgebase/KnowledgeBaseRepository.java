package com.harrydrummond.wikisite.knowledgebase;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface KnowledgeBaseRepository extends CrudRepository<KnowledgeBase, Long> {

    List<KnowledgeBase> findAllByOrderByRatingDesc();

    List<KnowledgeBase> findAllByTags(Tag tag);

    KnowledgeBase findByTitle(String title);

    List<KnowledgeBase> findAll();
}
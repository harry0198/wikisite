package com.harrydrummond.wikisite.repository;

import com.harrydrummond.wikisite.entity.KnowledgeBase;
import com.harrydrummond.wikisite.entity.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface KnowledgeBaseRepository extends CrudRepository<KnowledgeBase, Integer> {

    List<KnowledgeBase> findAllByOrderByRatingDesc();

    List<KnowledgeBase> findAllByTags(Tag tag);

    KnowledgeBase findByTitle(String title);
}
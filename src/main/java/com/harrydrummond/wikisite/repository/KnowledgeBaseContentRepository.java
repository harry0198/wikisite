package com.harrydrummond.wikisite.repository;

import com.harrydrummond.wikisite.entity.KnowledgeBase;
import com.harrydrummond.wikisite.entity.KnowledgeBaseContent;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface KnowledgeBaseContentRepository extends CrudRepository<KnowledgeBaseContent, Long> {

    List<KnowledgeBaseContent> findByKnowledgeBase(KnowledgeBase knowledgeBase);
}
package com.harrydrummond.wikisite.repository;

import com.harrydrummond.wikisite.entity.KnowledgeBase;
import com.harrydrummond.wikisite.entity.KnowledgeBaseContent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface KnowledgeBaseContentRepository extends CrudRepository<KnowledgeBaseContent, Long> {

    List<KnowledgeBaseContent> findByKnowledgeBase(KnowledgeBase knowledgeBase);
}
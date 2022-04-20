package com.harrydrummond.wikisite.knowledgebase.content;

import com.harrydrummond.wikisite.knowledgebase.KnowledgeBase;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface KnowledgeBaseContentRepository extends CrudRepository<KnowledgeBaseContent, Long> {

    List<KnowledgeBaseContent> findByKnowledgeBase(KnowledgeBase knowledgeBase);

    @Query("SELECT SUM(m.views) FROM KnowledgeBaseContent m")
    int selectViews();
}
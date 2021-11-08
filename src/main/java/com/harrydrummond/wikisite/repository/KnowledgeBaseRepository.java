package com.harrydrummond.wikisite.repository;

import com.harrydrummond.wikisite.entity.KnowledgeBase;
import org.springframework.data.repository.CrudRepository;

public interface KnowledgeBaseRepository extends CrudRepository<KnowledgeBase, Integer> {
}
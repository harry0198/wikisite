package com.harrydrummond.wikisite.knowledgebase;

import org.springframework.data.repository.CrudRepository;

public interface TagRepository extends CrudRepository<Tag, String> {
}
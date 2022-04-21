package com.harrydrummond.wikisite.articles;

import org.springframework.data.repository.CrudRepository;

public interface TagRepository extends CrudRepository<Tag, String> {
}
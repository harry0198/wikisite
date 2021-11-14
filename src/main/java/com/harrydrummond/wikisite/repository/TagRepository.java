package com.harrydrummond.wikisite.repository;

import com.harrydrummond.wikisite.entity.Tag;
import org.springframework.data.repository.CrudRepository;

public interface TagRepository extends CrudRepository<Tag, String> {
}
package com.harrydrummond.wikisite.articles;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArticleRepository extends CrudRepository<Article, Long> {

    List<Article> findAllByOrderByRatingDesc();

//    List<Article> findAllByTags(Tag tag);

    Article findByTitle(String title);

    List<Article> findAll();
}
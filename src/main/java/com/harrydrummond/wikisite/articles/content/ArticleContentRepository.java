package com.harrydrummond.wikisite.articles.content;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface ArticleContentRepository extends CrudRepository<ArticleContent, Long> {

    @Query("SELECT SUM(m.views) FROM ArticleContent m")
    int selectViews();
}
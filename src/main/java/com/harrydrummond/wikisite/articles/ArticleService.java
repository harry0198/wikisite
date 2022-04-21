package com.harrydrummond.wikisite.articles;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public Article loadArticleById(long id) {
        return articleRepository.findById(id).orElseThrow(() -> new IllegalStateException("Article does not exist with provided id"));
    }

    public List<Article> loadAllArticles() {
        return articleRepository.findAll();
    }
}
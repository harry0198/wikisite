package com.harrydrummond.wikisite.articles;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@AllArgsConstructor
public class ArticleService {

    @PersistenceContext
    private EntityManager entityManager;
    private final ArticleRepository articleRepository;

    public Article loadArticleById(long id) {
        return articleRepository.findById(id).orElseThrow(() -> new IllegalStateException("Article does not exist with provided id"));
    }

    public List<Article> loadAllArticles() {
        return articleRepository.findAll();
    }

////    @Transactional
//    public List<Article> searchArticles(String searchText, int pageNo, int resultsPerPage) {
//        FullTextQuery jpaQuery = searchArticlesQuery(searchText);
//
//        jpaQuery.setMaxResults(resultsPerPage);
//        jpaQuery.setFirstResult((pageNo-1) * resultsPerPage);
//
//        List<Article> userList = jpaQuery.getResultList();
//
//        return userList;
//    }
//
//    @Transactional
//    public int searchArticlesTotalCount(String searchText) {
//
//        FullTextQuery jpaQuery = searchArticlesQuery(searchText);
//
//        int articlesCount = jpaQuery.getResultSize();
//
//        return articlesCount;
//    }
//
//    private FullTextQuery searchArticlesQuery (String searchText) {
//
//        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
//        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
//                .buildQueryBuilder()
//                .forEntity(Article.class)
//                .get();
//
//        org.apache.lucene.search.Query luceneQuery = queryBuilder
//                .keyword()
//                .wildcard()
//                .onFields("username", "email", "userDetail.lastName")
//                .boostedTo(5f)
//                .andField("userDetail.firstName")
//                .matching(searchText + "*")
//                .createQuery();
//
//        FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, Article.class);
//
//        return jpaQuery;
//    }
}
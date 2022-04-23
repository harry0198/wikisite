package com.harrydrummond.wikisite.articles.search;

import com.harrydrummond.wikisite.articles.Article;
import lombok.AllArgsConstructor;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.engine.search.query.dsl.SearchQueryOptionsStep;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.search.loading.dsl.SearchLoadingOptionsStep;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Searches the index for articles. Uses hibernate search and lucene.
 *
 * The index may be modified between the retrieval of two pages. As a result of that modification,
 * it is possible that some hits change position, and end up being present on two subsequent pages.
 */
@AllArgsConstructor
@Service
public class ArticleSearcherService {

    private static final int PAGE_SIZE = 10;
    private static final int TIMEOUT_MS = 500;

    private final EntityManager entityManager;

    /**
     * Searches index for articles matching the queryString with a set page number and page size.
     *
     * Searches article's content fields, article tags and title.
     *
     * @param queryString String to use to search and match the index content with
     * @param pageNum Elements on provided page number to retrieve
     * @return A list of articles matching the query string on the provided page. Uses default page size.
     */
    @Transactional
    public List<Article> searchArticles(String queryString, int pageNum) {
        return searchArticles(queryString, pageNum, PAGE_SIZE);
    }

    /**
     * Searches index for articles matching the queryString with a set page number and page size.
     *
     * Searches article's content fields, article tags and title.
     *
     * @param queryString String to use to search and match the index content with
     * @param pageNum Elements on provided page number to retrieve
     * @param pageSize Size each page should be.
     * @return A list of articles matching the query string on the provided page.
     */
    @Transactional
    public List<Article> searchArticles(String queryString, int pageNum, int pageSize) {

        SearchResult<Article> result = searchArticlesQueryFragment(queryString)
                .fetch( calcOffset(pageNum,pageSize), pageSize );

        return result.hits();
    }

    /**
     * Searches index for articles matching the queryString with a set page number and page size.
     *
     * Searches article's content fields, article tags and title. Orders results by date newest to oldest
     * @param queryString String to use to search and match the index content with
     * @param pageNum Elements on provided page number to retrieve
     * @param pageSize Size each page should be.
     * @return A list of articles matching the query string on the provided page. Sorted by date, newest to oldest
     */
    @Transactional
    public List<Article> searchArticlesSortByDate(String queryString, int pageNum, int pageSize) {

        SearchResult<Article> result = searchArticlesQueryFragment(queryString)
                .sort( f -> f.field( "dateCreated" ).desc())
                .fetch( calcOffset(pageNum, pageSize), pageSize );

        return result.hits();
    }

    /**
     * Gets all articles in the index with set page number and size. Sorts by date, newest to oldest
     *
     * @param pageNum Elements on provided page number to retrieve
     * @param pageSize Size each page should be.
     * @return A list of all articles on the provided page. Sorted by date, newest to oldest
     */
    @Transactional
    public List<Article> getAllArticlesSortByDate(int pageNum, int pageSize) {

        SearchResult<Article> result = getAllArticlesQueryFragment()
                .sort( f -> f.field( "dateCreated" ).desc())
                .fetch( calcOffset(pageNum, pageSize), pageSize);

        return result.hits();
    }

    /**
     * Gets all articles in the index with set page number and size.
     *
     * @param pageNum Elements on provided page number to retrieve
     * @param pageSize Size each page should be.
     * @return A list of all articles on the provided page.
     */
    @Transactional
    public List<Article> getAllArticles(int pageNum, int pageSize) {

        SearchResult<Article> result = getAllArticlesQueryFragment()
                .fetch( calcOffset(pageNum, pageSize), pageSize);

        return result.hits();
    }

    private int calcOffset(int pageNum, int pageSize) {
        return (pageNum-1) * pageSize;
    }

    private SearchQueryOptionsStep<?, Article, SearchLoadingOptionsStep, ?, ?> searchArticlesQueryFragment(String query) {
        SearchSession searchSession = Search.session( entityManager );

        return searchSession.search(Article.class)
                .where( f -> f.match()
                        .field("title").boost(2.0f)
                        .field("tags.name" ).boost(1.25f)
                        .field( "possibleContents.content")
                        .matching(query))
                .failAfter(TIMEOUT_MS, TimeUnit.MILLISECONDS);
    }

    private SearchQueryOptionsStep<?, Article, SearchLoadingOptionsStep, ?, ?> getAllArticlesQueryFragment() {
        SearchSession searchSession = Search.session( entityManager );

        return searchSession.search(Article.class)
                .where(SearchPredicateFactory::matchAll)
                .failAfter(TIMEOUT_MS, TimeUnit.MILLISECONDS);
    }


}
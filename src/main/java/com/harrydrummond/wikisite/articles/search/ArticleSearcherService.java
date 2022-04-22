package com.harrydrummond.wikisite.articles.search;

import com.harrydrummond.wikisite.articles.Article;
import lombok.AllArgsConstructor;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@Service
public class ArticleSearcherService {

    private static final int PAGE_SIZE = 10;
    private static final int TIMEOUT_MS = 500;

    private final EntityManager entityManager;



    /**
     * Searches index for articles matching the queryString with a set page number and page size.
     * The index may be modified between the retrieval of two pages. As a result of that modification,
     * it is possible that some hits change position, and end up being present on two subsequent pages.
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
     * The index may be modified between the retrieval of two pages. As a result of that modification,
     * it is possible that some hits change position, and end up being present on two subsequent pages.
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
        int offset = (pageNum-1) * pageSize;

        SearchSession searchSession = Search.session( entityManager );

        SearchResult<Article> result = searchSession.search(Article.class)
                .where( f -> f.match()
                        .field("title").boost(2.0f)
                        .field("tags.name" ).boost(1.25f)
                        .field( "possibleContents.content")
                        .matching( queryString ) )
                .failAfter( TIMEOUT_MS, TimeUnit.MILLISECONDS )
                .fetch( offset, pageSize );

        return result.hits();
    }

    @Transactional
    public List<Article> searchArticlesSortByDate(String queryString, int pageNum, int pageSize) {
        int offset = (pageNum-1) * pageSize;

        SearchSession searchSession = Search.session( entityManager );

        SearchResult<Article> result = searchSession.search(Article.class)
                .where( f -> f.match()
                        .field("title").boost(2.0f)
                        .field("tags.name" ).boost(1.25f)
                        .field( "possibleContents.content")
                        .matching( queryString ) )
                .sort( f -> f.field( "dateCreated" ).desc())
                .failAfter( TIMEOUT_MS, TimeUnit.MILLISECONDS )
                .fetch( offset, pageSize );

        return result.hits();
    }

    @Transactional
    public List<Article> getAllArticlesSortByDate(int pageNum, int pageSize) {
        int offset = (pageNum-1) * pageSize;

        SearchSession searchSession = Search.session( entityManager );

        SearchResult<Article> result = searchSession.search(Article.class)
                .where(SearchPredicateFactory::matchAll)
                .sort( f -> f.field( "dateCreated" ).desc())
                .failAfter( TIMEOUT_MS, TimeUnit.MILLISECONDS )
                .fetch( offset, pageSize);

        return result.hits();
    }

    @Transactional
    public List<Article> getAllArticles(int pageNum, int pageSize) {
        int offset = (pageNum-1) * pageSize;

        SearchSession searchSession = Search.session( entityManager );

        SearchResult<Article> result = searchSession.search(Article.class)
                .where(SearchPredicateFactory::matchAll)
                .failAfter( TIMEOUT_MS, TimeUnit.MILLISECONDS )
                .fetch( offset, pageSize);

        return result.hits();
    }
}
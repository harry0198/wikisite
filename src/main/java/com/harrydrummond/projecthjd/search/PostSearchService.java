package com.harrydrummond.projecthjd.search;

import com.harrydrummond.projecthjd.posts.Post;
import com.harrydrummond.projecthjd.util.Pagination;
import lombok.AllArgsConstructor;
import org.hibernate.search.engine.search.predicate.dsl.*;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Service
public class PostSearchService {

    private final EntityManager entityManager;
    private static final int HITS_PER_PAGE = 50;

    @Transactional
    public Pagination<Post> findByString(String string, int pageNum) {
        return findByString(string, pageNum, HITS_PER_PAGE);
    }

    @Transactional
    public Pagination<Post> findByString(String string, int pageNum, int pageSize) {
        SearchSession searchSession = Search.session( entityManager );

        SearchResult<Post> result = searchSession.search( Post.class )
                .where(f -> f.match().fields("title", "description").matching(string))
                .fetch(getPageOffset(pageNum, pageSize), pageSize);

        return new Pagination<>(result.hits(), result.total().hitCount(), pageSize, pageNum);
    }

    @Transactional
    public Pagination<Post> findPopularThisMonth(int pageNum) {
        return findPopularThisMonth(pageNum, HITS_PER_PAGE);
    }
    @Transactional
    public Pagination<Post> findPopularThisMonth(int pageNum, int pageSize) {
        SearchSession searchSession = Search.session( entityManager );


        SearchResult<Post> result = searchSession.search( Post.class )
                .where(f -> f.matchAll().except(f.range().field("datePosted").lessThan(LocalDateTime.now().minusMonths(1))))
                .fetch(getPageOffset(pageNum, pageSize), pageSize);

        List<Post> copied = new ArrayList<>(result.hits());
        Collections.reverse(copied);
        return new Pagination<>(copied, result.total().hitCount(), pageSize, pageNum);

    }

    @Transactional
    public Pagination<Post> findRecent(int pageNum) {
        return findRecent(pageNum, HITS_PER_PAGE);
    }
    @Transactional
    public Pagination<Post> findRecent(int pageNum, int pageSize) {
        SearchSession searchSession = Search.session( entityManager );
        SearchResult<Post> result = searchSession.search( Post.class )
                .where(SearchPredicateFactory::matchAll)
                .sort(f -> f.field("datePosted"))
                .fetch(getPageOffset(pageNum, pageSize), pageSize);

        List<Post> copied = new ArrayList<>(result.hits());
        Collections.reverse(copied);
        return new Pagination<>(copied, result.total().hitCount(), pageSize, pageNum);
    }

    @Transactional
    public Pagination<Post> findMostViewed(int pageNum) {
        return findMostViewed(pageNum, HITS_PER_PAGE);
    }

    @Transactional
    public Pagination<Post> findMostViewed(int pageNum, int pageSize) {
        SearchSession searchSession = Search.session( entityManager );
        SearchResult<Post> result = searchSession.search( Post.class )
                .where(SearchPredicateFactory::matchAll)
                .sort(f -> f.field("views"))
                .fetch(getPageOffset(pageNum, pageSize), pageSize);

        List<Post> copied = new ArrayList<>(result.hits());
        Collections.reverse(copied);
        return new Pagination<>(copied, result.total().hitCount(), pageSize, pageNum);
    }

    private int getPageOffset(int pageNum, int pageSize) {
        return (pageNum-1) * pageSize;
    }

}
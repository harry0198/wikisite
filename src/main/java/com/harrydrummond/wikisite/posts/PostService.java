package com.harrydrummond.wikisite.posts;

import com.harrydrummond.wikisite.util.Pagination;
import lombok.AllArgsConstructor;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.engine.search.query.dsl.SearchQueryOptionsStep;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.search.loading.dsl.SearchLoadingOptionsStep;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class PostService {

    private static final int PAGE_SIZE = 6;
    private static final int TIMEOUT_MS = 500;

    @Autowired
    private final EntityManager entityManager;
    private final PostRepository postRepository;


    /**
     * Get post by id. If post with id is not in the database, an IllegalStateException is thrown.
     * @param id Id of post
     * @return Post found in database with ID.
     */
    public Post getPostById(long id) {
        return postRepository
                .findById(id)
                .orElseThrow(() -> new IllegalStateException("Post does not exist"));
    }

    /**
     * Saves post to database
     * @param post Post to save
     */
    public Post savePost(Post post) {
       return postRepository.save(post);
    }

    @Transactional
    public Pagination<Post> searchPosts(String queryString, int pageNum) {
        return searchPosts(queryString, pageNum, PAGE_SIZE);
    }

    /**
     * Searches index for Posts matching the queryString with a set page number and page size.
     *
     * Searches Post's content fields, Post tags and title.
     *
     * @param queryString String to use to search and match the index content with
     * @param pageNum Elements on provided page number to retrieve
     * @param pageSize Size each page should be.
     * @return A list of Posts matching the query string on the provided page.
     */
    @Transactional
    public Pagination<Post> searchPosts(String queryString, int pageNum, int pageSize) {

        SearchResult<Post> result = searchPostsQueryFragment(queryString)
                .fetch( calcOffset(pageNum,pageSize), pageSize );

        return new Pagination<>(result.hits(), result.total().hitCount(), pageSize);
    }

    /**
     * Searches index for Posts matching the queryString with a set page number and page size.
     *
     * Searches Post's content fields, Post tags and title. Orders results by date newest to oldest
     * @param queryString String to use to search and match the index content with
     * @param pageNum Elements on provided page number to retrieve
     * @param pageSize Size each page should be.
     * @return A list of Posts matching the query string on the provided page. Sorted by date, newest to oldest
     */
    @Transactional
    public Pagination<Post> searchPostsSortByDate(String queryString, int pageNum, int pageSize) {

        SearchResult<Post> result = searchPostsQueryFragment(queryString)
                .sort( f -> f.field( "dateCreated" ).desc())
                .fetch( calcOffset(pageNum, pageSize), pageSize );

        return new Pagination<>(result.hits(), result.total().hitCount(), pageSize);
    }

    /**
     * Gets all Posts in the index with set page number and size. Sorts by date, newest to oldest
     *
     * @param pageNum Elements on provided page number to retrieve
     * @param pageSize Size each page should be.
     * @return A list of all Posts on the provided page. Sorted by date, newest to oldest
     */
    @Transactional
    public Pagination<Post> getAllPostsSortByDate(int pageNum, int pageSize) {

        SearchResult<Post> result = getAllPostsQueryFragment()
                .sort( f -> f.field( "datePosted" ).desc())
                .fetch( calcOffset(pageNum, pageSize), pageSize);

        return new Pagination<>(result.hits(), result.total().hitCount(), pageSize);
    }

    /**
     * Gets all Posts in the index with set page number and size.
     *
     * @param pageNum Elements on provided page number to retrieve
     * @param pageSize Size each page should be.
     * @return A list of all Posts on the provided page.
     */
    @Transactional
    public Pagination<Post> getAllPosts(int pageNum, int pageSize) {

        SearchResult<Post> result = getAllPostsQueryFragment()
                .fetch( calcOffset(pageNum, pageSize), pageSize);

        return new Pagination<>(result.hits(), result.total().hitCount(), pageSize);
    }

    private int calcOffset(int pageNum, int pageSize) {
        return (pageNum-1) * pageSize;
    }

    private SearchQueryOptionsStep<?, Post, SearchLoadingOptionsStep, ?, ?> searchPostsQueryFragment(String query) {
        SearchSession searchSession = Search.session( entityManager );

        return searchSession.search(Post.class)
                .where( f -> f.match()
                        .field("title").boost(2.0f)
                        .field( "description")
                        .field("images.")
                        .matching(query))
                .failAfter(TIMEOUT_MS, TimeUnit.MILLISECONDS);
    }

    private SearchQueryOptionsStep<?, Post, SearchLoadingOptionsStep, ?, ?> getAllPostsQueryFragment() {
        SearchSession searchSession = Search.session( entityManager );

        return searchSession.search(Post.class)
                .where(SearchPredicateFactory::matchAll)
                .failAfter(TIMEOUT_MS, TimeUnit.MILLISECONDS);
    }
}
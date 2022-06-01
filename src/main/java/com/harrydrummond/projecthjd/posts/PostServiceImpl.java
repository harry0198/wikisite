package com.harrydrummond.projecthjd.posts;

import com.harrydrummond.projecthjd.posts.comment.Comment;
import com.harrydrummond.projecthjd.util.Pagination;
import lombok.AllArgsConstructor;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.engine.search.query.dsl.SearchQueryOptionsStep;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.search.loading.dsl.SearchLoadingOptionsStep;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@AllArgsConstructor
public class PostServiceImpl implements PostService {


    private final PostRepository postRepository;
    private final EntityManager entityManager;


    @Override
    public Optional<Post> getPostById(long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            Post p = post.get();
            p.getComments();
            p.getLikes();
            p.getSaves();
            for (Comment comment : p.getComments()) {
                comment.getUser();
            }
        }
        return post;
    }

    @Override
    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post updatePost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public void deletePost(long id) {
        postRepository.deleteById(id);
    }

    @Override
    public Pagination<Post> searchPostsByQuery(String query, int page, int pageSize) {
        SearchResult<Post> result = searchPostsQueryFragment(query)
                .fetch( calcOffset(page,pageSize), pageSize );

        return new Pagination<>(result.hits(), result.total().hitCount(), pageSize, page);
    }

    // Offset for pages
    private int calcOffset(int pageNum, int pageSize) {
        return (pageNum-1) * pageSize;
    }

    private SearchQueryOptionsStep<?, Post, SearchLoadingOptionsStep, ?, ?> searchPostsQueryFragment(String query) {
        SearchSession searchSession = Search.session( entityManager );

        return searchSession.search(Post.class)
                .where( f -> f.match()
                        .field("title").boost(2.0f)
                        .field( "description")
                        .field("images.alt")
                        .matching(query))
                .failAfter(500, TimeUnit.MILLISECONDS);
    }
}
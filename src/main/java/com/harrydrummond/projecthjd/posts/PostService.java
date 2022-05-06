package com.harrydrummond.projecthjd.posts;

import com.harrydrummond.projecthjd.util.Pagination;

import java.util.List;
import java.util.Optional;

public interface PostService {

    Optional<Post> getPostById(long id);

    Post savePost(Post post);

    List<Post> getAllPosts();

    Post updatePost(Post post);

    void deletePost(long id);

    Pagination<Post> searchPostsByQuery(String query, int page, int pageSize);
}
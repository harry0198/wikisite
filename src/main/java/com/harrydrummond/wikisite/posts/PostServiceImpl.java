package com.harrydrummond.wikisite.posts;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {


    private final PostRepository postRepository;


    @Override
    public Optional<Post> getPostById(long id) {
        return postRepository.findById(id);
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
}
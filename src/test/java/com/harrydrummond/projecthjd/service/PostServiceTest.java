package com.harrydrummond.projecthjd.service;

import com.harrydrummond.projecthjd.user.*;
import com.harrydrummond.projecthjd.user.roles.Role;
import com.harrydrummond.projecthjd.user.roles.UserRole;
import com.harrydrummond.projecthjd.posts.image.Image;
import com.harrydrummond.projecthjd.posts.Post;
import com.harrydrummond.projecthjd.posts.PostRepository;
import com.harrydrummond.projecthjd.posts.PostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostServiceImpl postService;

    private Post post;

    @BeforeEach
    public void generatePost() {
        User user = new User();
        user.setEmail("example@email.com");
        user.setUsername("harry");
        user.setUserRoles(Set.of(new UserRole(Role.USER)));
        user.setProvider(Provider.LOCAL);
        user.setDateCreated(LocalDateTime.now());
        user.setLocked(false);
        user.setEnabled(false);
        user.setId(1L);

        post = new Post();
        post.setDatePosted(LocalDateTime.now());
        post.setPoster(user);
        post.setDescription("Random desc");
        post.setTitle("Title");
        post.setId(1L);
    }

    @Test
    void getPostById() {
        given(postRepository.findById(1L)).willReturn(Optional.of(post));

        Post foundPost = postService.getPostById(1L).get();

        assertThat(foundPost).isNotNull();
    }

    @Test
    void savePost() {
        given(postRepository.save(post)).willReturn(post);

        Post savedPost = postService.savePost(post);

        assertThat(savedPost).isNotNull();
    }

    @Test
    void getAllPosts() {
        Post post1 = new Post();
        post1.setTitle("title");
        post1.setId(2L);
        post1.setDescription("as");

        given(postRepository.findAll()).willReturn(List.of(post,post1));

        // when -  action or the behaviour that we are going test
        List<Post> postList = postService.getAllPosts();

        // then - verify the output
        assertThat(postList).isNotNull();
        assertThat(postList.size()).isEqualTo(2);
    }

//    @Test
//    void updatePost() {
//
//        given(postRepository.save(post)).willReturn(post);
//
//        User user = new User();
//        user.setEmail("example@email.com");
//        user.setUsername("harry");
//        user.setUserRoles(Set.of(new UserRole(Role.ADMIN)));
//        user.setProvider(Provider.LOCAL);
//        user.setDateCreated(LocalDateTime.now());
//        user.setLocked(false);
//        user.setEnabled(false);
//        user.setId(1L);
//
//        Set<UserLikes> userLikesSet = new HashSet<>();
//        UserLikes ul = new UserLikes();
//        ul.setUser(user);
//        ul.setPost(post);
//        userLikesSet.add(ul);
//
//        Set<UserSaves> userSavesSet = new HashSet<>();
//        UserSaves us = new UserSaves();
//        us.setUser(user);
//        us.setPost(post);
//        userSavesSet.add(us);
//
//        Set<Image> imageSet = new HashSet<>();
//        Image image = new Image();
//        image.setId(4L);
//        image.setPost(post);
//        image.setPath("path/some");
//        image.setAlt("alt");
//
//        post.setDatePosted(LocalDateTime.now());
//        post.setId(2L);
//        post.setPoster(user);
//        post.setDescription("desc");
//        post.setTitle("title");
//        post.setLikes(userLikesSet);
//        post.setSaves(userSavesSet);
//        post.setImages(imageSet);
//
//        Post updatedPost = postService.updatePost(post);
//
//        assertThat(updatedPost.getId()).isEqualTo(2L);
//        assertThat(updatedPost.getDescription()).isEqualTo("desc");
//        assertThat(updatedPost.getTitle()).isEqualTo("title");
//        assertThat(updatedPost.getPoster()).isEqualTo(user);
//        assertThat(updatedPost.getLikes()).isEqualTo(userLikesSet);
//        assertThat(updatedPost.getSaves()).isEqualTo(userSavesSet);
//        assertThat(updatedPost.getImages()).isEqualTo(imageSet);
//        assertThat(updatedPost.getDatePosted()).isEqualTo(LocalDateTime.now());
//    }

    @Test
    void deletePost() {
        willDoNothing().given(postRepository).deleteById(1L);

        postService.deletePost(1L);

        verify(postRepository, times(1)).deleteById(1L);
    }
}
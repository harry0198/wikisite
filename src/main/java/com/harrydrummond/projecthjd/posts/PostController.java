package com.harrydrummond.projecthjd.posts;

import com.harrydrummond.projecthjd.posts.comment.Comment;
import com.harrydrummond.projecthjd.posts.comment.CommentService;
import com.harrydrummond.projecthjd.posts.comment.dto.CommentRequestDTO;
import com.harrydrummond.projecthjd.posts.dto.PostInfoDTO;
import com.harrydrummond.projecthjd.posts.dto.PostRequestDTO;
import com.harrydrummond.projecthjd.posts.dto.PostSearchDTO;
import com.harrydrummond.projecthjd.posts.image.Image;
import com.harrydrummond.projecthjd.posts.image.ImageDTO;
import com.harrydrummond.projecthjd.posts.image.ImageGetDTO;
import com.harrydrummond.projecthjd.posts.image.ImageService;
import com.harrydrummond.projecthjd.user.User;
import com.harrydrummond.projecthjd.user.UserService;
import com.harrydrummond.projecthjd.user.roles.Role;
import com.harrydrummond.projecthjd.util.Pagination;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@RestController
public class PostController {

    private final CommentService commentService;
    private final PostService postService;
    private final UserService userService;
    private final ImageService imageService;

    @PostMapping(value = "/api/post/new")
    public ResponseEntity<Post> savePost(PostRequestDTO postDTO) {

        Optional<User> userOptional = userService.getUserById(postDTO.getAuthorId());
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = userOptional.get();

        Post post = new Post();
        post.setDatePosted(new Date(System.currentTimeMillis()));
        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());
        post.setPoster(user);

        Set<Image> images = new HashSet<>();
        for (ImageDTO imageRequest : postDTO.getImages()) {
            Image image = new Image();
            image.setPost(post);
            image.setAlt(imageRequest.getAlt());
            image.setOrderNo(imageRequest.getOrder());
            File savedFile;
            try {
                savedFile = imageService.saveImageToFileOnly(image, imageRequest.getFile()).toFile();
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            image.setPath(savedFile.getPath());
        }

        post.setImages(images);
        postService.savePost(post);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @PutMapping("/api/post/{uid}")
    public ResponseEntity<Post> updatePostById(@PathVariable long uid, PostRequestDTO postDTO) {
        Optional<Post> postOptional = postService.getPostById(uid);
        if (postOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Post post = postOptional.get();

        Set<Image> images = new HashSet<>();
        for (ImageDTO imageRequest : postDTO.getImages()) {
            Image image = new Image();
            image.setPost(post);
            image.setAlt(imageRequest.getAlt());
            image.setOrderNo(imageRequest.getOrder());
            File savedFile;
            try {
                savedFile = imageService.saveImageToFileOnly(image, imageRequest.getFile()).toFile();
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            image.setPath(savedFile.getPath());
        }

        post.setTitle(postDTO.getTitle());
        post.setImages(images);
        post.setDescription(post.getDescription());
        //todo author?

        postService.updatePost(post);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/post/{uid}")
    public ResponseEntity<PostInfoDTO> getPostById(@PathVariable long uid) {
        Optional<Post> postOptional = postService.getPostById(uid);
        if (postOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Post post = postOptional.get();
        PostInfoDTO dto = populatePostGetDTO(post);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping("/api/post/{uid}/comment")
    public ResponseEntity<Void> deleteComment(@PathVariable long uid, @AuthenticationPrincipal User user, long commentId) {
        Optional<Post> postOptional = postService.getPostById(uid);
        if (postOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Post post = postOptional.get();
        Optional<Comment> commentOptional = commentService.getCommentById(commentId);
        if (commentOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Comment comment = commentOptional.get();
        // if requesting user is owner of post OR requesting user is owner of comment OR requesting user is an admin
        if (user.getId().equals(post.getPoster().getId()) || user.getId().equals(comment.getUser().getId()) || user.containsRole(Role.ADMIN)) {
            post.removeCommentById(commentId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }


    }

    @PostMapping("/api/post/{uid}/comment")
    public ResponseEntity<Void> saveComment(@PathVariable long uid, @AuthenticationPrincipal User user, CommentRequestDTO commentRequestDTO) {
        Optional<Post> postOptional = postService.getPostById(uid);
        if (postOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Post post = postOptional.get();
        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .comment(commentRequestDTO.getComment())
                .datePosted(LocalDateTime.now())
                .build();
        if (commentRequestDTO.getParentCommentId() != null) {
            Optional<Comment> repliedCommentOptional = post.getComments().stream().filter(x -> x.getId() == commentRequestDTO.getParentCommentId()).findAny();
            if (repliedCommentOptional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Comment repliedComment = repliedCommentOptional.get();
            repliedComment.addReply(comment);
        } else {
            post.addComment(comment);
        }

        postService.savePost(post);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/post/search")
    public ResponseEntity<PostSearchDTO> searchPosts(String query, @Min(0) int page, @Min(1) @Max(50) int pageSize) {
        Pagination<Post> posts = postService.searchPostsByQuery(query, page, pageSize);

        PostInfoDTO[] postInfoDTO = new PostInfoDTO[posts.getPageSize()];
        int i = 0;
        for (Post post : posts.getContent()) {
            postInfoDTO[i] = populatePostGetDTO(post);
            i++;
        }

        PostSearchDTO postSearchDTO = PostSearchDTO.builder()
                .pageNum(page)
                .pageSize(posts.getPageSize())
                .totalPages(posts.calcTotalPages())
                .postInfoDTOS(postInfoDTO)
                .build();

        return new ResponseEntity<>(postSearchDTO, HttpStatus.OK);
    }

    private PostInfoDTO populatePostGetDTO(Post post) {

        ImageGetDTO[] images = new ImageGetDTO[post.getImages().size()];
        int i = 0;
        for (Image image : post.getImages()) {
            ImageGetDTO imageDTO = ImageGetDTO.builder()
                    .alt(image.getAlt())
                    .path(image.getPath())
                    .order(image.getOrderNo())
                    .build();
            images[i] = imageDTO;
            i++;
        }

        return PostInfoDTO.builder()
                .id(post.getId())
                .datePosted(post.getDatePosted())
                .title(post.getTitle())
                .description(post.getDescription())
                .likes(post.getLikes().size())
                .saves(post.getSaves().size())
                .images(images)
                .posterId(post.getPoster().getId())
                .build();
    }
}
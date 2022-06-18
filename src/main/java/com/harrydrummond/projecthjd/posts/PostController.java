package com.harrydrummond.projecthjd.posts;

import com.harrydrummond.projecthjd.posts.comment.Comment;
import com.harrydrummond.projecthjd.posts.comment.CommentService;
import com.harrydrummond.projecthjd.posts.dto.PostInfoDTO;
import com.harrydrummond.projecthjd.posts.dto.PostRequestDTO;
import com.harrydrummond.projecthjd.posts.dto.PostSearchDTO;
import com.harrydrummond.projecthjd.posts.image.Image;
import com.harrydrummond.projecthjd.posts.image.ImageGetDTO;
import com.harrydrummond.projecthjd.posts.image.ImageService;
import com.harrydrummond.projecthjd.user.User;
import com.harrydrummond.projecthjd.user.UserService;
import com.harrydrummond.projecthjd.user.roles.Role;
import com.harrydrummond.projecthjd.util.Pagination;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Controller
public class PostController {

    private final CommentService commentService;
    private final PostService postService;
    private final UserService userService;
    private final ImageService imageService;

    @PostMapping(value = "/api/post/new",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String savePostForm(@AuthenticationPrincipal User requestingUser, PostRequestDTO postRequestDTO) {

        ResponseEntity<Post> postResponseEntity = savePost(requestingUser, postRequestDTO);
        if (postResponseEntity.getStatusCode() == HttpStatus.OK && postResponseEntity.getBody() != null) {
            return "redirect:/post/view/" + postResponseEntity.getBody().getId();
        }

        return "redirect:/search";
    }

    @PostMapping(value = "/api/post/new")
    public ResponseEntity<Post> savePost(@AuthenticationPrincipal User user, @RequestBody PostRequestDTO postDTO) {
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (postDTO.getDescription() == null || postDTO.getTitle() == null || postDTO.getImage().getSize() <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Post post = new Post();
        post.setDatePosted(LocalDateTime.now());
        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());
        post.setPoster(user);

        Image image = new Image();
        image.setPost(post);
        image.setAlt("");
        image.setOrderNo(1);

        boolean success = saveImage(postDTO.getImage(), image);
        if (!success) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        post.setImages(Set.of(image));
        postService.savePost(post);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    private boolean saveImage(MultipartFile imageFile, Image image) {

        File savedFile;
        try {
            savedFile = imageService.saveImageToFileOnly(image, imageFile).toFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        image.setPath(savedFile.getPath());
        return true;
    }
    @PostMapping(value = "/api/post/update",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String updatePostForm(@AuthenticationPrincipal User requestingUser, PostRequestDTO postRequestDTO) {
        ResponseEntity<Post> postResponseEntity = updatePost(requestingUser, postRequestDTO);
        if (postResponseEntity.getStatusCode() == HttpStatus.OK && postResponseEntity.getBody() != null) {
            return "redirect:/post/view/" + postResponseEntity.getBody().getId();
        }

        return "redirect:/search";
    }

    @PostMapping("/api/post/update")
    public ResponseEntity<Post> updatePost(@AuthenticationPrincipal User user, @RequestBody PostRequestDTO postRequestDTO) {

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<Post> postOptional = postService.getPostById(postRequestDTO.getId());
        if (postOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Post post = postOptional.get();

        if (postRequestDTO.getImage().getSize() > 0) {
            Image image = new Image();
            image.setPost(post);
            image.setOrderNo(1);
            image.setAlt("");
            saveImage(postRequestDTO.getImage(), image);
            imageService.saveImage(image);
            post.getImages().clear();
            post.getImages().add(image);
        }
        if (postRequestDTO.getTitle() != null) {
            post.setTitle(postRequestDTO.getTitle());
        }
        if (postRequestDTO.getDescription() != null) {
            post.setDescription(postRequestDTO.getDescription());
        }
        postService.updatePost(post);
        return new ResponseEntity<>(post, HttpStatus.OK);

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

    @DeleteMapping("/api/post/{uid}")
    public ResponseEntity<Object> deletePostById(@AuthenticationPrincipal User user, @PathVariable long uid) {
        Optional<Post> postOptional = postService.getPostById(uid);
        if (postOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Post post = postOptional.get();

        if (user == null || user.getId().equals(post.getPoster().getId()) || user.containsRole(Role.ADMIN)) {
            postService.deletePost(uid);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/api/post/{uid}/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable long uid, @AuthenticationPrincipal User user, @PathVariable int commentId) {
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
        if (user == null || user.getId().equals(post.getPoster().getId()) || user.getId().equals(comment.getUser().getId()) || user.containsRole(Role.ADMIN)) {
            post.removeCommentById(commentId);
            commentService.deleteComment(commentId);
            postService.savePost(post);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }


    }

    @PatchMapping("/api/post/{uid}/comment/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable long uid, @AuthenticationPrincipal User user, @PathVariable int commentId, @RequestBody String commentStr) {
        Optional<Comment> commentOptional = commentService.getCommentById(commentId);
        if (commentOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Comment comment = commentOptional.get();
        if (!comment.getUser().getId().equals(user.getId())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        comment.setComment(commentStr);
        commentService.updateComment(comment);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/post/{uid}/comment")
    public ResponseEntity<Void> saveComment(@PathVariable long uid, @AuthenticationPrincipal User user, @RequestBody String commentStr) {
        Optional<Post> postOptional = postService.getPostById(uid);
        if (postOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Post post = postOptional.get();
        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .comment(commentStr)
                .datePosted(LocalDateTime.now())
                .build();
        post.addComment(comment);

        commentService.saveComment(comment);
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
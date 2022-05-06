package com.harrydrummond.projecthjd.posts;

import com.harrydrummond.projecthjd.posts.dto.PostInfoDTO;
import com.harrydrummond.projecthjd.posts.dto.PostRequestDTO;
import com.harrydrummond.projecthjd.posts.dto.PostSearchDTO;
import com.harrydrummond.projecthjd.posts.image.Image;
import com.harrydrummond.projecthjd.posts.image.ImageDTO;
import com.harrydrummond.projecthjd.posts.image.ImageGetDTO;
import com.harrydrummond.projecthjd.posts.image.ImageService;
import com.harrydrummond.projecthjd.util.Pagination;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@RestController
public class PostController {

    private final PostService postService;
    private final ImageService imageService;

    @PostMapping("/api/post/new")
    public ResponseEntity<Post> savePost(PostRequestDTO postDTO) {
        Post post = new Post();
        post.setDatePosted(new Date(System.currentTimeMillis()));
        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());

        Set<Image> images = new HashSet<>();
        for (ImageDTO imageRequest : postDTO.getImages()) {
            Image image = new Image();
            image.setPost(post);
            image.setAlt(imageRequest.getAlt());
            File savedFile;
            try {
                savedFile = imageService.saveImageToFileOnly(image, imageRequest.getFile());
            } catch (IOException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            image.setPath(savedFile.getPath());
        }

        post.setImages(images);
        postService.savePost(post);
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
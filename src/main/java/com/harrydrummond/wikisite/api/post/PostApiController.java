package com.harrydrummond.wikisite.api.post;

import com.harrydrummond.wikisite.appuser.User;
import com.harrydrummond.wikisite.posts.*;
import com.harrydrummond.wikisite.util.FileUtil;
import com.harrydrummond.wikisite.util.exceptions.MissingFileException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@RestController
public class PostApiController {

    private final PostServiceImpl postService;


    @PostMapping("/api/v1/post/new")
    public ResponseEntity<?> createPostRequest(@AuthenticationPrincipal User user, @RequestBody PostRequest postRequest) {

        try {

            Set<Image> imageSet = new HashSet<>();

            Post post = new Post();
            post.setDatePosted(new Date(System.currentTimeMillis()));
            post.setDescription(postRequest.getDescription());
            post.setTitle(postRequest.getTitle());
            post.setPoster(user);

            Set<MultipartFile> files = new HashSet<>();
            String path = String.format("static/user-%s/posts/images/", user.getId());

            for (ImageRequest imageRequest : postRequest.getImageRequests()) {
                FileUtil.validateImage(imageRequest.getFile());

                Image image = new Image();
                image.setAlt(imageRequest.getAlt());
                image.setPath(path);
                image.setPost(post);

                imageSet.add(image);
                files.add(imageRequest.getFile());
            }
            // Re-looped to ensure nothing goes wrong with the above and mitigates excess files being saved when an
            // exception occurs.
            for (MultipartFile file : files) {
                FileUtil.saveFile(file, user, new File(path).toPath());
            }

            post.setImages(imageSet);
            postService.savePost(post);

            return ResponseEntity.status(HttpStatus.CREATED).body("Profile image uploaded successfully!");
        } catch (MissingFileException me) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing file");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error");
        }
    }


}
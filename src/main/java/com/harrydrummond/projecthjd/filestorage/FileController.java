package com.harrydrummond.projecthjd.filestorage;

import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class FileController {

    private ResourceLoader resourceLoader;

    @GetMapping("/uploads/post/images/{sub}/{fileName}")
    public ResponseEntity<Resource> getFile(@PathVariable String sub, @PathVariable String fileName) {
        Resource resource = resourceLoader.getResource("file:uploads/post/images/" + sub + "/" + fileName);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"temp").body(resource);
    }
}
package com.harrydrummond.projecthjd.posts.image;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Builder
@ToString
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ImageDTO {
    private MultipartFile file;
    private String alt;
    private int order;
}
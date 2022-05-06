package com.harrydrummond.projecthjd.posts.image;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Builder
@ToString
@Data
public class ImageDTO {
    private final MultipartFile file;
    private final String alt;
}
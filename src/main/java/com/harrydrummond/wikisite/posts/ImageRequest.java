package com.harrydrummond.wikisite.posts;

import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@ToString
@Data
public class ImageRequest {
    private final MultipartFile file;
    private final String alt;
}
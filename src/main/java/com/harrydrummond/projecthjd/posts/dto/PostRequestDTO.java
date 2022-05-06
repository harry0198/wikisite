package com.harrydrummond.projecthjd.posts.dto;

import com.harrydrummond.projecthjd.posts.image.ImageDTO;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class PostRequestDTO {
    private final String title;
    private final String description;
    private final ImageDTO[] images;
}
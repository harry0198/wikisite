package com.harrydrummond.projecthjd.posts.dto;

import com.harrydrummond.projecthjd.posts.image.ImageDTO;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class PostRequestDTO {
    private String title;
    private String description;
    private ImageDTO[] images;
    private int authorId;
}
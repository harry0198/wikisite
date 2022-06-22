package com.harrydrummond.projecthjd.posts.dto;

import com.harrydrummond.projecthjd.validators.ImageConstraint;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@ToString
@Data
public class PostRequestDTO {
    private String title;
    private String description;

    @ImageConstraint
    private MultipartFile image;
    private Long id;
}
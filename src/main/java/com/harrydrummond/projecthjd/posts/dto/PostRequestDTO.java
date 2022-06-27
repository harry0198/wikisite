package com.harrydrummond.projecthjd.posts.dto;

import com.harrydrummond.projecthjd.validators.ImageConstraint;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;

@ToString
@Data
public class PostRequestDTO {

    @Size(min = 3, max = 60)
    private String title;
    @Size(max = 1500, min = 12)
    private String description;

    @ImageConstraint
    private MultipartFile image;
    private Long id;
}
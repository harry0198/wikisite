package com.harrydrummond.projecthjd.posts.dto;

import com.harrydrummond.projecthjd.validators.image.ImageConstraint;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;

@ToString
@Data
public class ImageDTO {

    @ImageConstraint
    private MultipartFile media;
    private int order;
    @Size(max = 500)
    private String alt;
}
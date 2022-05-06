package com.harrydrummond.projecthjd.posts.image;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class ImageGetDTO {
    private final String alt;
    private final String path;
}
package com.harrydrummond.projecthjd.posts.image;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class ImageGetDTO {
    private String alt;
    private String path;
    private int order;

    public ImageGetDTO(Image image) {
        this.alt = image.getAlt();
        this.path = image.getPath();
        this.order = -1;
    }
}
package com.harrydrummond.projecthjd.posts.image;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
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
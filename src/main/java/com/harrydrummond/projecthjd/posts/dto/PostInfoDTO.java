package com.harrydrummond.projecthjd.posts.dto;

import com.harrydrummond.projecthjd.posts.image.ImageGetDTO;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Builder
@ToString
@Data
public class PostInfoDTO {

    private final long id;
    private final String title;
    private final String description;
    private final ImageGetDTO[] images;
    private final int likes;
    private final int saves;
    private final long posterId;
    private final Date datePosted;

}
package com.harrydrummond.projecthjd.posts;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class PostRequest {
    private final String title;
    private final String description;
    private final ImageRequest[] imageRequests;
}
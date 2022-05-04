package com.harrydrummond.wikisite.api.post;

import lombok.Data;

@Data
public class PostLikeRequest {
    private final int postId;
    private final boolean like;
}
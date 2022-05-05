package com.harrydrummond.projecthjd.api.post;

import lombok.Data;

@Data
public class PostSaveRequest {
    private final int postId;
    private final boolean save;
}
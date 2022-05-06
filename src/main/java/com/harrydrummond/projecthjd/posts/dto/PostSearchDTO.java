package com.harrydrummond.projecthjd.posts.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@ToString
@Data
public class PostSearchDTO {

    private final int pageSize;
    private final int pageNum;
    private final int totalPages;
    private final PostInfoDTO[] postInfoDTOS;
}
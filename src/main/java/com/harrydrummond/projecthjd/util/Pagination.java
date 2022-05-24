package com.harrydrummond.projecthjd.util;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class Pagination<T> {

    private final List<T> content;
    private final long totalHits;
    private final int pageSize;

    private final int pageNum;

    public int calcTotalPages() {
        return (int) Math.ceil((double) totalHits / pageSize);
    }


}
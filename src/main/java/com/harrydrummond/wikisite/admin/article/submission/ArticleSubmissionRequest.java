package com.harrydrummond.wikisite.admin.article.submission;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.NonNull;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ArticleSubmissionRequest {

    @NonNull
    private final String title;
    @NonNull
    private final String tagLine;
}
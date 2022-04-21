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
public class ArticleContentSubmissionRequest {

    @NonNull
    private final String versionDescription;
    @NonNull
    private final String version;
    @NonNull
    private final String content;
    @NonNull
    private final int articleId;
}
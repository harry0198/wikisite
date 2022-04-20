package com.harrydrummond.wikisite.admin.article.submission;

import com.harrydrummond.wikisite.articles.Article;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController("/api/v1/components/article/submission")
public class ArticleSubmissionController {

    @PostMapping("")
    public String submitNewArticle(Principal principal, Article article) {
        UserDetails userDetails = (UserDetails) principal;
        return "";
    }
}
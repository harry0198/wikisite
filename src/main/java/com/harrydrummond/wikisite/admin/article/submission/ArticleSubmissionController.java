package com.harrydrummond.wikisite.admin.article.submission;

import com.harrydrummond.wikisite.appuser.AppUser;
import com.harrydrummond.wikisite.appuser.AppUserService;
import com.harrydrummond.wikisite.articles.Article;
import com.harrydrummond.wikisite.articles.ArticleRepository;
import com.harrydrummond.wikisite.articles.content.ArticleContent;
import com.harrydrummond.wikisite.articles.content.ArticleContentRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.sql.Date;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/components/article/submission")
public class ArticleSubmissionController {

    private final AppUserService appUserService;
    private final ArticleRepository articleRepository;
    private final ArticleContentRepository articleContentRepository;

    @PostMapping("article")
    public ResponseEntity submitNewArticle(ArticleSubmissionRequest articleRequest) {
        Article article = Article.builder()
                .title(articleRequest.getTitle())
                .tagLine(articleRequest.getTagLine())
                .dateCreated(new Date(System.currentTimeMillis()))
                .build();

        articleRepository.save(article);
        return ResponseEntity.ok().build();
    }

    @PostMapping("content")
    public ResponseEntity submitNewArticleContent(Principal principal, ArticleContentSubmissionRequest articleContentRequest) {
//        AppUser user = appUserService.loadUserByUsername(principal.getName());
//        Article article = articleRepository.findById((long) articleContentRequest.getArticleId())
//                .orElseThrow(() -> new IllegalStateException("Article to submit content to did not exist!"));
//
//        ArticleContent articleContent = ArticleContent.builder()
//                .appUser(user)
//                .dateCreated(new Date(System.currentTimeMillis()))
//                .content(articleContentRequest.getContent())
//                .versionString(articleContentRequest.getVersion())
//                .versionInfo(articleContentRequest.getVersionDescription())
//                .knowledgeBase(article)
//                .views(0L)
//                .build();
//
//        articleContentRepository.save(articleContent);
//        System.out.println("contenet ehre");
        return ResponseEntity.ok().build();
    }

}
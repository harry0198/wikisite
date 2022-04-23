package com.harrydrummond.wikisite.admin;

import com.harrydrummond.wikisite.admin.article.submission.ArticleContentSubmissionRequest;
import com.harrydrummond.wikisite.admin.article.submission.ArticleSubmissionRequest;
import com.harrydrummond.wikisite.appuser.AppUser;
import com.harrydrummond.wikisite.appuser.AppUserService;
import com.harrydrummond.wikisite.articles.Article;
import com.harrydrummond.wikisite.articles.ArticleRepository;
import com.harrydrummond.wikisite.articles.ArticleService;
import com.harrydrummond.wikisite.articles.content.ArticleContent;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@AllArgsConstructor
@Controller
public class AdminController {

    private final ArticleService articleService;
    private final AppUserService appUserService;

    @GetMapping("/admin")
    public String getSearchIndexPage(Model model) {
        model.addAttribute("articles", articleService.loadAllArticles());
        return "admin/admin-panel";
    }
//
//    @GetMapping("/admin/articles/submission/preview")
//    public String previewArticleSubmission(Model model, Principal principal, ArticleContentSubmissionRequest articleContentRequest) {
//        Article article = articleService.loadArticleById(articleContentRequest.getArticleId());
//        AppUser appUser = appUserService.loadUserByUsername(principal.getName());
//
//        ArticleContent articleContent = ArticleContent.builder()
//                .views(0L)
//                .knowledgeBase(article)
//                .versionInfo(articleContentRequest.getVersionDescription())
//                .versionString(articleContentRequest.getVersion())
//                .appUser(appUser)
//                .content(articleContentRequest.getContent())
//                .build();
//
//        model.addAttribute("kb", article);
//        model.addAttribute("kbcontent", articleContent);
//
//        return "kbtemplate";
//    }
}
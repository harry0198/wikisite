package com.harrydrummond.wikisite.search;

import com.harrydrummond.wikisite.articles.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class ArticleSearchController {

    public static final int USERS_PER_PAGE = 20;

    @Autowired
    ArticleService articleService;

    @GetMapping("/admin-panel")
    public String showAdminPanel(@RequestParam(value = "search", required = false) String searchText,
                                 @RequestParam(value = "pageNo", required = false) Integer pageNo,
                                 ModelMap model) {

        if (searchText == null && pageNo == null) {
            return "index";
        }

        if (searchText != null && pageNo == null) {
            pageNo = 1;
            model.put("pageNo", 1);
        }

//        model.addAttribute("resultsCount", articleService.searchArticlesResultsCount(searchText));
//
//        model.addAttribute("pageCount", articleService.searchArticlesPagesCount(searchText, USERS_PER_PAGE));
//
//        System.out.println(articleService
//                .searchArticles(searchText, pageNo, USERS_PER_PAGE));
        return "admin/admin-panel";
    }
}
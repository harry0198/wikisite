package com.harrydrummond.wikisite.articles.search;

import com.harrydrummond.wikisite.articles.Article;
import com.harrydrummond.wikisite.articles.ArticleService;
import com.harrydrummond.wikisite.util.Validate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;


@Controller
@AllArgsConstructor
public class ArticleSearchController {

    private final ArticleSearcherService articleSearcherService;
    private final ArticleService articleService;

    /**
     * Gets the homepage for searching the knowledgebase and applies relevant attributes
     * @param model Model
     * @return Index page template
     */
    @GetMapping("/")
    public String getSearchIndexPage(Model model) {
        model.addAttribute("totalViews", articleService.getTotalViews());
        model.addAttribute("exploreArticles", articleSearcherService.getAllArticles(1,3));
        return "index";
    }


    @GetMapping("/search")
    public String querySearch(@RequestParam(required = false) String query, @RequestParam(required = false) Integer page, Model model) {

        model.addAttribute("maxInputLength", Validate.Options.DEFAULT_MAX_INPUT_LENGTH);

        if (query == null || query.isEmpty() || !Validate.validateInputLength(query)) {
            query = ""; // shouldn't return anything on search.
        }
        if (page == null) {
            page = 1;
        }
        List<Article> results = articleSearcherService.searchArticles(query, page);

        if (results.size() <= 0) {
            model.addAttribute("topArticles", articleSearcherService.getAllArticles(1,3));
            model.addAttribute("recentArticles", articleSearcherService.getAllArticlesSortByDate(1, 3));
        }
        model.addAttribute("resultsSize", ((Collection<?>) results).size());
        model.addAttribute("kbs", results);
        model.addAttribute("query", query);

        return "search";
    }
}
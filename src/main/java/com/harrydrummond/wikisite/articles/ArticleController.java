package com.harrydrummond.wikisite.articles;

import com.harrydrummond.wikisite.articles.content.ArticleContent;
import com.harrydrummond.wikisite.articles.content.ArticleContentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleContentRepository kbContentRepository;

    @GetMapping(value={"/kb/view/{title}", "/articles/{title}"})
    public String getKnowledgeBase(@PathVariable String title, @RequestParam(required = false) String version, Model model) {
        title = title.replaceAll("-", " ");
        Article kb = articleService.findByTitle(title);
        if (kb == null) return "error";

        ArticleContent content;
        if (version != null) {
            content = kb.getArticleContentFromVersion(version);
        } else {
            content = kb.getLatestArticleContent();
        }
        model.addAttribute("kb", kb);
        model.addAttribute("kbcontent", content);

        content.incrementViews();
        kbContentRepository.save(content);

        return "kbtemplate";
    }



}
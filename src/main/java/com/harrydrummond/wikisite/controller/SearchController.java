package com.harrydrummond.wikisite.controller;

import com.harrydrummond.wikisite.ResultViewType;
import com.harrydrummond.wikisite.entity.KnowledgeBase;
import com.harrydrummond.wikisite.model.KnowledgeBaseModel;
import com.harrydrummond.wikisite.util.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class SearchController {

    private final KnowledgeBaseModel kbModel;


    @Autowired
    public SearchController(KnowledgeBaseModel kbModel) {
        this.kbModel = kbModel;
    }

    /**
     * Gets the homepage for searching the knowledgebase and applies relevant attributes
     * @param model Model
     * @return Index page template
     */
    @GetMapping("/")
    public String getSearchIndexPage(Model model) {
        addSearchModelAttributes(kbModel.getAllKnowledgeBasesFromIndex(), model);
        model.addAttribute("preferredView", ResultViewType.GRID);
        return "index";
    }


    @GetMapping("/kb/search")
    public String querySearch(@RequestParam(required = false) String query, Model model) {
        if (query == null || query.isEmpty() || !Validate.validateInputLength(query)) {
            return getSearchIndexPage(model);
        }

        List<KnowledgeBase> results = kbModel.findKnowledgeBasesByQueryFromIndex(query);

        addSearchModelAttributes(results, model);
        model.addAttribute("preferredView", ResultViewType.LIST);
        model.addAttribute("query", query);

        return "search";
    }

    private static Model addSearchModelAttributes(Iterable<KnowledgeBase> kbs, Model model) {
        model.addAttribute("kbs", kbs);
        model.addAttribute("resultsSize", ((Collection<?>) kbs).size());
        model.addAttribute("maxInputLength", Validate.Options.DEFAULT_MAX_INPUT_LENGTH);

        return model;
    }
}
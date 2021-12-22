package com.harrydrummond.wikisite.controller;

import com.harrydrummond.wikisite.ResultViewType;
import com.harrydrummond.wikisite.entity.KnowledgeBase;
import com.harrydrummond.wikisite.model.IndexModel;
import com.harrydrummond.wikisite.model.IndexTask;
import com.harrydrummond.wikisite.repository.KnowledgeBaseRepository;
import com.harrydrummond.wikisite.util.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class SearchController {

    private final IndexModel indexModel;

    @Autowired
    public SearchController(KnowledgeBaseRepository knowledgeBaseRepository, IndexModel indexModel) {
        this.indexModel = indexModel;
        this.indexModel.scanKnowledgeBaseToIndex(knowledgeBaseRepository.findAll());

        final int PERIOD = (1000 * 60) * 30; // 30 mins
        new Timer().scheduleAtFixedRate(new IndexTask(indexModel, knowledgeBaseRepository), PERIOD, PERIOD);
    }

    /**
     * Gets the homepage for searching the knowledgebase and applies relevant attributes
     * @param model Model
     * @return Index page template
     */
    @GetMapping("/")
    public String getSearchIndexPage(Model model) {
        List<KnowledgeBase> kb = indexModel.getAllKnowledgeBases();
        kb = kb.stream().sorted().collect(Collectors.toList());
        addSearchModelAttributes(kb, model);
        model.addAttribute("preferredView", ResultViewType.GRID);
        return "index";
    }


    @GetMapping("/kb/search")
    public String querySearch(@RequestParam(required = false) String query, Model model) {
        if (query == null || query.isEmpty() || Validate.validateInputLength(query)) {
            return getSearchIndexPage(model);
        }

        List<KnowledgeBase> results = indexModel.searchByString(query);

        addSearchModelAttributes(results, model);
        model.addAttribute("preferredView", ResultViewType.LIST);
        model.addAttribute("query", query);

        return "index";
    }

    private static Model addSearchModelAttributes(Iterable<KnowledgeBase> kbs, Model model) {
        model.addAttribute("kbs", kbs);
        model.addAttribute("resultsSize", ((Collection<?>) kbs).size());
        model.addAttribute("maxInputLength", Validate.Options.DEFAULT_MAX_INPUT_LENGTH);

        return model;
    }
}
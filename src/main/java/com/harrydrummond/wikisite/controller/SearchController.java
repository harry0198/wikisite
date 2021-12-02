package com.harrydrummond.wikisite.controller;

import com.harrydrummond.wikisite.ResultViewType;
import com.harrydrummond.wikisite.entity.KnowledgeBase;
import com.harrydrummond.wikisite.entity.Tag;
import com.harrydrummond.wikisite.model.IndexModel;
import com.harrydrummond.wikisite.model.IndexTask;
import com.harrydrummond.wikisite.repository.KnowledgeBaseRepository;
import com.harrydrummond.wikisite.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class SearchController {

    private final IndexModel indexModel;

    @Autowired
    public SearchController(KnowledgeBaseRepository knowledgeBaseRepository) {
        this.indexModel = new IndexModel();
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
        addSearchModelAttributes(kb, model);
        model.addAttribute("preferredView", ResultViewType.GRID);
        return "index";
    }


    @GetMapping("/kb/search")
    public String querySearch(@RequestParam(required = false) String query, Model model) {
        if (query == null || query.isEmpty()) {
            return getSearchIndexPage(model);
        }

        List<KnowledgeBase> results = indexModel.searchByString(query);

        addSearchModelAttributes(results, model);
        model.addAttribute("preferredView", ResultViewType.LIST);

        return "index";
    }

    private static Model addSearchModelAttributes(Iterable<KnowledgeBase> kbs, Model model) {
        model.addAttribute("kbs", kbs);
        model.addAttribute("resultsSize", ((Collection<?>) kbs).size());

        return model;
    }
}
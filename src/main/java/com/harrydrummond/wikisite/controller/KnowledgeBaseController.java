package com.harrydrummond.wikisite.controller;

import com.harrydrummond.wikisite.ResultViewType;
import com.harrydrummond.wikisite.entity.KnowledgeBase;
import com.harrydrummond.wikisite.entity.KnowledgeBaseContent;
import com.harrydrummond.wikisite.entity.Tag;
import com.harrydrummond.wikisite.repository.KnowledgeBaseRepository;
import com.harrydrummond.wikisite.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class KnowledgeBaseController {

    private final KnowledgeBaseRepository knowledgeBaseRepository;
    private final TagRepository tagRepository;

    @Autowired
    public KnowledgeBaseController(KnowledgeBaseRepository knowledgeBaseRepository, TagRepository tagRepository) {
        this.knowledgeBaseRepository = knowledgeBaseRepository;
        this.tagRepository = tagRepository;
    }

    @GetMapping("/")
    public String search(Model model) {
        Iterable<KnowledgeBase> kb = knowledgeBaseRepository.findAll();
        addSearchModelAttributes(kb, model);
        model.addAttribute("preferredView", ResultViewType.GRID);
        return "index";
    }
//TODO Should probably add a filter for the contact us page and about etc
    @GetMapping("/kb/search")
    public String querySearch(@RequestParam(required = false) String query, Model model) {
        if (query == null || query.isEmpty()) {
            return search(model);
        }

        Set<KnowledgeBase> kb = new HashSet<>();
        String[] rs = query.toUpperCase().split(" ");
        Iterable<Tag> tags = tagRepository.findAllById(List.of(rs));
        for (Tag tag : tags) {
            kb.addAll(tag.getTaggedKnowledgeBases());
        }

        addSearchModelAttributes(kb, model);
        model.addAttribute("preferredView", ResultViewType.LIST);

        return "index";
    }

    @GetMapping("/kb/view/{title}")
    public String getKnowledgeBase(@PathVariable(required = true) String title, @RequestParam(required = false) String version, Model model) {
        title = title.replaceAll("-", " ");
        KnowledgeBase kb = knowledgeBaseRepository.findByTitle(title);
        System.out.println("Before error");
        if (kb == null) return "error";

        KnowledgeBaseContent content;
        if (version != null) {
            System.out.println("Version not null");
            content = kb.getKnowledgeBaseContentFromVersion(version);
        } else {
            System.out.println("Getting latest");
            content = kb.getLatestKnowledgeBaseContent();
        }
        model.addAttribute("kb", kb);
        model.addAttribute("kbcontent", content);

        return "kbtemplate";
    }

    private Model addSearchModelAttributes(Iterable<KnowledgeBase> kbs, Model model) {
        model.addAttribute("kbs", kbs);
        model.addAttribute("resultsSize", ((Collection<?>) kbs).size());

        return model;
    }
}
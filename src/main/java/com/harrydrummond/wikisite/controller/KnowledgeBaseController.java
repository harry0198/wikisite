package com.harrydrummond.wikisite.controller;

import com.harrydrummond.wikisite.entity.KnowledgeBase;
import com.harrydrummond.wikisite.entity.KnowledgeBaseContent;
import com.harrydrummond.wikisite.repository.KnowledgeBaseContentRepository;
import com.harrydrummond.wikisite.repository.KnowledgeBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class KnowledgeBaseController {

    private final KnowledgeBaseContentRepository knowledgeBaseContentRepository;
    private final KnowledgeBaseRepository knowledgeBaseRepository;

    @Autowired
    public KnowledgeBaseController(KnowledgeBaseRepository knowledgeBaseRepository, KnowledgeBaseContentRepository knowledgeBaseContentRepository) {
        this.knowledgeBaseRepository = knowledgeBaseRepository;
        this.knowledgeBaseContentRepository = knowledgeBaseContentRepository;
    }

    @GetMapping("/kb/view/{title}")
    public String getKnowledgeBase(@PathVariable(required = true) String title, @RequestParam(required = false) String version, Model model) {
        title = title.replaceAll("-", " ");
        KnowledgeBase kb = knowledgeBaseRepository.findByTitle(title);
        if (kb == null) return "error";

        KnowledgeBaseContent content;
        if (version != null) {
            content = kb.getKnowledgeBaseContentFromVersion(version);
        } else {
            content = kb.getLatestKnowledgeBaseContent();
        }
        model.addAttribute("kb", kb);
        model.addAttribute("kbcontent", content);

        content.incrementViews();
        knowledgeBaseContentRepository.save(content);

        return "kbtemplate";
    }

}
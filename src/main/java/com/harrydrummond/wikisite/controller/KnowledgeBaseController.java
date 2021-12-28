package com.harrydrummond.wikisite.controller;

import com.harrydrummond.wikisite.entity.KnowledgeBase;
import com.harrydrummond.wikisite.entity.KnowledgeBaseContent;
import com.harrydrummond.wikisite.model.KnowledgeBaseModel;
import com.harrydrummond.wikisite.repository.KnowledgeBaseContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class KnowledgeBaseController {

    private final KnowledgeBaseModel kbModel;
    private final KnowledgeBaseContentRepository kbContentRepository;

    @Autowired
    public KnowledgeBaseController(KnowledgeBaseModel kbModel, KnowledgeBaseContentRepository kbContentRepository) {
        this.kbModel = kbModel;
        this.kbContentRepository = kbContentRepository;
    }

    @GetMapping("/kb/view/{title}")
    public String getKnowledgeBase(@PathVariable(required = true) String title, @RequestParam(required = false) String version, Model model) {
        title = title.replaceAll("-", " ");
        KnowledgeBase kb = kbModel.findByTitle(title);
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
        kbContentRepository.save(content);

        return "kbtemplate";
    }

}
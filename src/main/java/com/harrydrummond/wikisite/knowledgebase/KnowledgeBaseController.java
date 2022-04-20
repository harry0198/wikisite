package com.harrydrummond.wikisite.knowledgebase;

import com.harrydrummond.wikisite.knowledgebase.content.KnowledgeBaseContent;
import com.harrydrummond.wikisite.knowledgebase.content.KnowledgeBaseContentRepository;
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

    @GetMapping(value={"/kb/view/{title}", "/articles/{title}"})
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
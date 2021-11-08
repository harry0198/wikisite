package com.harrydrummond.wikisite.controller;

import com.harrydrummond.wikisite.entity.KnowledgeBase;
import com.harrydrummond.wikisite.repository.KnowledgeBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class KnowledgeBaseController {

    @Autowired
    private KnowledgeBaseRepository knowledgeBaseRepository;

    @GetMapping("/test")
    @ResponseBody
    public List<KnowledgeBase> test() {
        return (List<KnowledgeBase>)knowledgeBaseRepository.findAll();
    }
}
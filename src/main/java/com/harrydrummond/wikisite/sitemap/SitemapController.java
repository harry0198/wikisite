package com.harrydrummond.wikisite.sitemap;

import com.harrydrummond.wikisite.knowledgebase.KnowledgeBase;
import com.harrydrummond.wikisite.knowledgebase.IndexModel;
import com.harrydrummond.wikisite.sitemap.XmlUrl;
import com.harrydrummond.wikisite.sitemap.XmlUrlSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
public class SitemapController {

    private final IndexModel indexModel;

    @Autowired
    public SitemapController(IndexModel indexModel) {
        this.indexModel = indexModel;
    }

    @RequestMapping(value = "/sitemap.xml", method = RequestMethod.GET, produces = { "application/xml",
            "text/xml" })
    @ResponseBody
    public XmlUrlSet getSitemap() {
        XmlUrlSet xmlUrlSet = new XmlUrlSet();
        for (KnowledgeBase allKnowledgeBase : indexModel.getAllKnowledgeBases()) {
            create(xmlUrlSet, allKnowledgeBase.getKnowledgeBaseUrlSafe(), getPriorityRating(allKnowledgeBase.getRating()), allKnowledgeBase.getDateCreated());
        }

        return xmlUrlSet;
    }

    private XmlUrl.Priority getPriorityRating(int rating) {
        if (rating > 10) {
            return XmlUrl.Priority.HIGH;
        } else {
            return XmlUrl.Priority.MEDIUM;
        }
    }

    private void create(XmlUrlSet xmlUrlSet, String link, XmlUrl.Priority priority, Date date) {
        xmlUrlSet.addUrl(new XmlUrl("https://www.vesudatutorials.com/kb/view/" + link, priority, date));
    }



}
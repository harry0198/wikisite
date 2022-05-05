package com.harrydrummond.wikisite.sitemap;

import com.harrydrummond.wikisite.posts.Post;
import com.harrydrummond.wikisite.posts.PostServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@AllArgsConstructor
@Controller
public class SitemapController {

    private final PostServiceImpl postService;


    @RequestMapping(value = "/sitemap.xml", method = RequestMethod.GET, produces = { "application/xml",
            "text/xml" })
    @ResponseBody
    public XmlUrlSet getSitemap() {
        XmlUrlSet xmlUrlSet = new XmlUrlSet();
//        for (Post post : postService.getAllPosts(1, Integer.MAX_VALUE).getContent()) {
//            create(xmlUrlSet, post.getTitleUrlSafe(), XmlUrl.Priority.MEDIUM, post.getDatePosted());
//        }//TODO

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
        xmlUrlSet.addUrl(new XmlUrl("https://www.vesudatutorials.com/post/" + link, priority, date));
    }



}
package com.harrydrummond.projecthjd.sitemap;

import com.harrydrummond.projecthjd.posts.Post;
import com.harrydrummond.projecthjd.posts.PostServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

@AllArgsConstructor
@Controller
public class SitemapController {

    private final PostServiceImpl postService;


    @RequestMapping(value = "/sitemap.xml", method = RequestMethod.GET, produces = { "application/xml",
            "text/xml" })
    @ResponseBody
    public XmlUrlSet getSitemap() {
        XmlUrlSet xmlUrlSet = new XmlUrlSet();
        for (Post post : postService.getAllPosts()) {
            create(xmlUrlSet, post.getId(), XmlUrl.Priority.MEDIUM, post.getDatePosted());
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

    private void create(XmlUrlSet xmlUrlSet, long link, XmlUrl.Priority priority, LocalDateTime date) {
        xmlUrlSet.addUrl(new XmlUrl("https://www.thedevblog.co.uk/post/view/" + link, priority, date));
    }



}
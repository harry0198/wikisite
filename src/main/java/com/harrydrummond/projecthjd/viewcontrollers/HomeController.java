package com.harrydrummond.projecthjd.viewcontrollers;

import com.harrydrummond.projecthjd.posts.Post;
import com.harrydrummond.projecthjd.posts.PostService;
import com.harrydrummond.projecthjd.posts.dto.PostRequestDTO;
import com.harrydrummond.projecthjd.search.PostSearchService;
import com.harrydrummond.projecthjd.user.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@AllArgsConstructor
@Controller
public class HomeController {

    private final PostService postService;
    private final PostSearchService postSearchService;

    @GetMapping
    public String getHome(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "pages/home";
    }

    @GetMapping("/explore")
    public String explore(@AuthenticationPrincipal User user, Model model, @RequestParam(required=true,defaultValue="1") Integer page) {
        model.addAttribute("page", page);
        model.addAttribute("user", user);
        model.addAttribute("posts", postSearchService.findPopularThisMonth(page));
        model.addAttribute("title", "What's Hot \uD83D\uDD25");
        model.addAttribute("subtitle", "Top picks for the most viewed showcases this month!");

        return "pages/explore";
    }

    @GetMapping("/recent")
    public String recent(@AuthenticationPrincipal User user, Model model, @RequestParam(required=true,defaultValue="1") Integer page) {
        model.addAttribute("page", page);
        model.addAttribute("user", user);
        model.addAttribute("posts", postSearchService.findRecent(page));
        model.addAttribute("title", "Recently Posted ⏳︎");
        model.addAttribute("subtitle", "The most recent showcases across the site!");

        return "pages/explore";
    }

    @GetMapping("/most-viewed")
    public String mostViewed(@AuthenticationPrincipal User user, Model model, @RequestParam(required=true,defaultValue="1") Integer page) {
        model.addAttribute("page", page);
        model.addAttribute("user", user);
        model.addAttribute("posts", postSearchService.findMostViewed(page));
        model.addAttribute("title", "Most Viewed \uD83D\uDCC8︎");
        model.addAttribute("subtitle", "The most viewed showcases of all time! Ranked most viewed to least viewed.");

        return "pages/explore";
    }

    @GetMapping("/search")
    public String search(@AuthenticationPrincipal User user, Model model, String query, @RequestParam(required=true,defaultValue="1") Integer page) {
        model.addAttribute("user", user);
        model.addAttribute("page", page);
        model.addAttribute("query", query);
        if (query != null) {
            model.addAttribute("posts", postSearchService.findByString(query, page));
        } else {
            model.addAttribute("posts", postSearchService.findPopularThisMonth(page));
        }
        String params = "?";
        if (query != null) {
            params += "query="+query;
        }
        if (params.equalsIgnoreCase("?")) {
            params += "page=";
        } else {
            params += "&page=";
        }
        model.addAttribute("uriForPageParams", params);

        return "pages/search";
    }

    @GetMapping("/post/new")
    public String newPost(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("postDTO", new PostRequestDTO());
        Post post = new Post();
        post.setPoster(user);
        model.addAttribute("post", post);

        return "pages/create-post";
    }

    @GetMapping("/post/view/{postId}")
    public String viewPost(@AuthenticationPrincipal User user, Model model, @PathVariable Integer postId) {
        model.addAttribute("user", user);
        model.addAttribute("posts", postSearchService.findPopularThisMonth(1, 3));
        return "pages/post-view";
    }
}
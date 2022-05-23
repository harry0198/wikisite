package com.harrydrummond.projecthjd.viewcontrollers;

import com.harrydrummond.projecthjd.posts.Post;
import com.harrydrummond.projecthjd.posts.PostService;
import com.harrydrummond.projecthjd.posts.dto.PostRequestDTO;
import com.harrydrummond.projecthjd.user.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@AllArgsConstructor
@Controller
public class HomeController {

    private final PostService postService;

    @GetMapping
    public String getHome(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "pages/home";
    }

    @GetMapping("/explore")
    public String explore(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("posts", postService.getAllPosts());

        return "pages/explore";
    }

    @GetMapping("/search")
    public String search(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);

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
}
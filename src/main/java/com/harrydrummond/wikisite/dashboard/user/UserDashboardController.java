package com.harrydrummond.wikisite.dashboard.user;

import com.harrydrummond.wikisite.appuser.AppUser;
import com.harrydrummond.wikisite.posts.PostService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@AllArgsConstructor
@Controller
public class UserDashboardController {

    private final PostService postService;

    @GetMapping("/user/dashboard")
    public String getUserDashboard(Model model, @AuthenticationPrincipal AppUser appUser) {
        model.addAttribute("user", appUser);
        model.addAttribute("posts", postService.getAllPosts(1,6).getContent());
        model.addAttribute("suggested_posts", postService.getAllPostsSortByDate(1,6).getContent());
        return "pages/dashboard";
    }

    @GetMapping("/user/dashboard/settings")
    public String getUserSettingsDashboard(Model model, @AuthenticationPrincipal AppUser appUser) {
        return "pages/dashboard-settings";
    }

    @GetMapping("/user/dashboard/saved")
    public String getUserSavedDashboard(Model model, @AuthenticationPrincipal AppUser appUser) {
        model.addAttribute("posts", postService.getAllPosts(1,6).getContent());

        return "pages/dashboard-saved";
    }

    @GetMapping("/user/dashboard/publish")
    public String getUserPublishDashboard(Model model, @AuthenticationPrincipal AppUser appUser) {
        return "pages/dashboard-publish";
    }
}
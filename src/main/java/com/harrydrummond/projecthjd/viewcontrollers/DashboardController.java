package com.harrydrummond.projecthjd.viewcontrollers;

import com.harrydrummond.projecthjd.user.User;
import com.harrydrummond.projecthjd.user.dto.UserDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class DashboardController {

    @GetMapping({"", "/general"})
    public String getDashboard(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "pages/dashboard/dashboard-general";
    }

    @GetMapping("/edit-profile")
    public String getSettingsForDashboard(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "pages/dashboard/dashboard-editprofile";
    }

    @GetMapping("/notifications")
    public String getSavedForDashboard(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "pages/dashboard/dashboard-notifications";
    }

    @GetMapping("/publish")
    public String getPublishForDashboard(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "pages/dashboard-publish";
    }
}
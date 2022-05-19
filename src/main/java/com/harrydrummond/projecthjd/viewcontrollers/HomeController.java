package com.harrydrummond.projecthjd.viewcontrollers;

import com.harrydrummond.projecthjd.user.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping
    public String getHome(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "pages/home";
    }

    @GetMapping("/explore")
    public String explore(Model model) {

        return "pages/explore";
    }
}
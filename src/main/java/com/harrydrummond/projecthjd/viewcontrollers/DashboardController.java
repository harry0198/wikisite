package com.harrydrummond.projecthjd.viewcontrollers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @GetMapping
    public String getDashboard(Model model) {
        return "pages/dashboard-settings";
    }

    @GetMapping("/settings")
    public String getSettingsForDashboard(Model model) {
        return "pages/dashboard-settings";
    }

    @GetMapping("/saved")
    public String getSavedForDashboard(Model model) {
        return "pages/dashboard-saved";
    }

    @GetMapping("/publish")
    public String getPublishForDashboard(Model model) {
        return "pages/dashboard-publish";
    }
}
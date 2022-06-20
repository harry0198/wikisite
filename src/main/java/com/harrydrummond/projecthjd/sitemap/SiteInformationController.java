package com.harrydrummond.projecthjd.sitemap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SiteInformationController {

    @GetMapping("/tos")
    public String tos(Model model) {
        return "/pages/site-info/tos";
    }
    @GetMapping("/disclaimer")
    public String disclaimer(Model model) {
        return "/pages/site-info/disclaimer";
    }
    @GetMapping("/privacy")
    public String privacy(Model model) {
        return "/pages/site-info/privacy";
    }
}
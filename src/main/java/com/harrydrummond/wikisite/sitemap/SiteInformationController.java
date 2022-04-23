package com.harrydrummond.wikisite.sitemap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SiteInformationController {

    @GetMapping("/privacy")
    public String getPrivacyPolicy() {
        return "privacy-policy";
    }

    @GetMapping("/cookies")
    public String getCookiesPolicy() {
        return "cookies-policy";
    }
}
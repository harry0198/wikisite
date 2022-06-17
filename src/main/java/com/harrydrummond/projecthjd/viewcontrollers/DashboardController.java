package com.harrydrummond.projecthjd.viewcontrollers;

import com.harrydrummond.projecthjd.user.User;
import com.harrydrummond.projecthjd.user.UserService;
import com.harrydrummond.projecthjd.user.dto.UserDTO;
import com.harrydrummond.projecthjd.user.preferences.Preference;
import com.harrydrummond.projecthjd.user.preferences.PreferencesRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/account")
public class DashboardController {

    private final PreferencesRepository preferencesRepository;
    private final UserService userService;

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
        model.addAttribute("brand", user.containsPreference(Preference.BRAND_INFO));
        model.addAttribute("promotions", user.containsPreference(Preference.PROMOTIONS));
        model.addAttribute("accountSummary", user.containsPreference(Preference.ACCOUNT_SUMMARY));
        return "pages/dashboard/dashboard-notifications";
    }

    @GetMapping("/manage")
    public String getPublishForDashboard(@AuthenticationPrincipal User user, Model model) {
        userService.loadPreferences(user);
        model.addAttribute("user", user);
        return "pages/dashboard/dashboard-manage";
    }
}
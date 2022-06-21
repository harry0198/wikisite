package com.harrydrummond.projecthjd;

import com.harrydrummond.projecthjd.filestorage.FileStorageService;
import com.harrydrummond.projecthjd.user.preferences.Preference;
import com.harrydrummond.projecthjd.user.preferences.Preferences;
import com.harrydrummond.projecthjd.user.preferences.PreferencesRepository;
import com.harrydrummond.projecthjd.user.roles.Role;
import com.harrydrummond.projecthjd.user.roles.RoleRepository;
import com.harrydrummond.projecthjd.user.roles.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.annotation.Resource;
import java.util.Locale;

@AllArgsConstructor
@SpringBootApplication
public class StartPointApplication extends WebMvcConfigurerAdapter implements CommandLineRunner, WebMvcConfigurer {


    @Resource
    private final FileStorageService storageService;

    private final RoleRepository roleRepository;
    private final PreferencesRepository preferencesRepository;

    public static void main(String[] args) {
        SpringApplication.run(StartPointApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        storageService.init();

        UserRole userRole = new UserRole();
        userRole.setRole(Role.USER);

        UserRole userAdminRole = new UserRole();
        userAdminRole.setRole(Role.ADMIN);

        roleRepository.save(userRole);
        roleRepository.save(userAdminRole);

        Preferences preferences1 = new Preferences();
        preferences1.setPreference(Preference.ACCOUNT_SUMMARY);

        Preferences preferences2 = new Preferences();
        preferences2.setPreference(Preference.BRAND_INFO);

        Preferences preferences3 = new Preferences();
        preferences3.setPreference(Preference.PROMOTIONS);

        preferencesRepository.save(preferences1);
        preferencesRepository.save(preferences2);
        preferencesRepository.save(preferences3);
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.ENGLISH);
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

}
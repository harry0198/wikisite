package com.harrydrummond.projecthjd;

import com.harrydrummond.projecthjd.filestorage.FileStorageService;
import com.harrydrummond.projecthjd.user.preferences.Preference;
import com.harrydrummond.projecthjd.user.preferences.Preferences;
import com.harrydrummond.projecthjd.user.preferences.PreferencesRepository;
import com.harrydrummond.projecthjd.user.roles.Role;
import com.harrydrummond.projecthjd.user.roles.RoleRepository;
import com.harrydrummond.projecthjd.user.roles.UserRole;
import lombok.AllArgsConstructor;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.schema.management.SearchSchemaManager;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.util.Locale;

@AllArgsConstructor
@SpringBootApplication
public class StartPointApplication extends WebMvcConfigurerAdapter implements CommandLineRunner, WebMvcConfigurer {

    private final EntityManager entityManager;

    public static void main(String[] args) {
        SpringApplication.run(StartPointApplication.class, args);
    }


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        SearchSession searchSession = Search.session( entityManager );
        SearchSchemaManager schemaManager = searchSession.schemaManager();
        schemaManager.dropAndCreate();
        searchSession.massIndexer().startAndWait();


//            UserRole userRole = new UserRole();
//            userRole.setRole(Role.USER);
//            roleRepository.save(userRole);
//
//            UserRole userAdminRole = new UserRole();
//            userAdminRole.setRole(Role.ADMIN);
//            roleRepository.save(userAdminRole);


//            Preferences preferences2 = new Preferences();
//            preferences2.setPreference(Preference.BRAND_INFO);
//            preferencesRepository.save(preferences2);
//
//
//
//            Preferences preferences1 = new Preferences();
//            preferences1.setPreference(Preference.ACCOUNT_SUMMARY);
//            preferencesRepository.save(preferences1);
//
//
//
//            Preferences preferences3 = new Preferences();
//            preferences3.setPreference(Preference.PROMOTIONS);
//
//            preferencesRepository.save(preferences3);

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
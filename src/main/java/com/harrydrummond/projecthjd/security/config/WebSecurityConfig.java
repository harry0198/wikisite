package com.harrydrummond.projecthjd.security.config;

import com.harrydrummond.projecthjd.user.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@AllArgsConstructor
@EnableWebSecurity

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserServiceImpl userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/account", "/account/**", "/post/new", "post/edit/**", "/api/post/*/**", "/api/post/new")
                .authenticated()
                .anyRequest().permitAll()
                .and()
                .oauth2Login()
                .userInfoEndpoint().userService(userService)
                .and().and()
                .logout()
                .logoutSuccessUrl("/");

//        http.csrf().disable();
//        http.headers().frameOptions().disable();
    }
}
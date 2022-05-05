package com.harrydrummond.wikisite.security.config;

import com.harrydrummond.wikisite.appuser.UserServiceImpl;
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
//        http
//                .csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/admin/**","/api/v1/components/article/submission/**")
//                .authenticated().antMatchers("/**")
//                .permitAll().and()
//                .oauth2Login().and().httpBasic();
        http.csrf().disable().authorizeRequests()
                .antMatchers("/admin/**")
                .authenticated()
                .anyRequest().permitAll()
                .and()
                .oauth2Login()
                .userInfoEndpoint().userService(userService);
    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.au;
//    }






//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(principleExtractor(null));
//        auth.userDetailsService()
//    }
//
//    @Bean
//    public DaoAuthenticationProvider daoAuthenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(appUserService);
//        return provider;
//    }
}
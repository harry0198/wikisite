package com.harrydrummond.wikisite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;

@SpringBootApplication
public class WikisiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(WikisiteApplication.class, args);
    }



}
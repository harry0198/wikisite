package com.harrydrummond.wikisite;

import com.harrydrummond.wikisite.articles.Article;
import lombok.AllArgsConstructor;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;

import javax.persistence.EntityManager;
import java.awt.print.Book;

@AllArgsConstructor
@SpringBootApplication
public class WikisiteApplication implements ApplicationRunner {

    private final EntityManager entityManager;

    public static void main(String[] args) {
        SpringApplication.run(WikisiteApplication.class, args);
    }


    @Transactional
    @Override
    public void run(ApplicationArguments args) throws Exception {
        SearchSession searchSession = Search.session( entityManager );

        MassIndexer indexer = searchSession.massIndexer( Article.class )
                .threadsToLoadObjects( 7 );

        indexer.startAndWait();
    }
}
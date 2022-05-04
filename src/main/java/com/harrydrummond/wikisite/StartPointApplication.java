package com.harrydrummond.wikisite;

import com.harrydrummond.wikisite.posts.Post;
import lombok.AllArgsConstructor;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@AllArgsConstructor
@SpringBootApplication
public class StartPointApplication implements ApplicationRunner {

    private final EntityManager entityManager;

    public static void main(String[] args) {
        SpringApplication.run(StartPointApplication.class, args);
    }


    @Transactional
    @Override
    public void run(ApplicationArguments args) throws Exception {
        SearchSession searchSession = Search.session( entityManager );

        MassIndexer indexer = searchSession.massIndexer( Post.class )
                .threadsToLoadObjects( 7 );

        indexer.startAndWait();
    }
}
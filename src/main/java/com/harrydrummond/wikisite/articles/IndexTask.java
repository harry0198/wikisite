package com.harrydrummond.wikisite.articles;

import org.apache.lucene.store.Directory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.TimerTask;

public class IndexTask extends TimerTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexTask.class);

    private final IndexModel indexModel;
    private final ArticleRepository knowledgeBaseRepository;

    public IndexTask(final IndexModel indexModel, final ArticleRepository knowledgeBaseRepository) {
        this.indexModel = indexModel;
        this.knowledgeBaseRepository = knowledgeBaseRepository;
    }

    @Override
    public void run() {

        LOGGER.info(String.format("Running Indexing task at %s", new Date(System.currentTimeMillis())));
        Directory directory = IndexModel.getNewDirectory();

        LOGGER.info(String.format("Scanning to new directory begin at %s", new Date(System.currentTimeMillis())));
        indexModel.scanAndWriteTo(directory, knowledgeBaseRepository.findAll());
        LOGGER.info(String.format("Scanning to new directory finish at %s", new Date(System.currentTimeMillis())));
        indexModel.setDirectory(directory);
        LOGGER.info("Directory indexing completed!");
    }
}
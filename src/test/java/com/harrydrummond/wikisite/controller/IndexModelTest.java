package com.harrydrummond.wikisite.controller;

import com.harrydrummond.wikisite.knowledgebase.KnowledgeBase;
import com.harrydrummond.wikisite.knowledgebase.content.KnowledgeBaseContent;
import com.harrydrummond.wikisite.knowledgebase.IndexModel;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class IndexModelTest {

    @Autowired private IndexModel indexModel;

    @Test
    public void givenSearchQueryWhenFetchedDocumentThenCorrect() {
        KnowledgeBase kb = quickMakeKnowledgeBaseWithContent(1, "Example Title", "Searchable content for querying here! Hello World");
        indexModel.scanKnowledgeBaseToIndex(kb);

        List<KnowledgeBase> documents = indexModel.searchByString("lots and lots of random irrelevant things BOOM HELLO WORLD okay");
        assertEquals(
                1L,
                documents.get(0).getId());
    }

    @Test
    public void testSetDirectory() {

        indexModel.scanKnowledgeBaseToIndex(quickMakeKnowledgeBaseWithContent(1,"Test Set Directory", "example search content"));

        assertEquals(1L, indexModel.searchByString("example search lots and lots of otehr things").get(0).getId());

        Directory directory = new ByteBuffersDirectory();

        indexModel.scanAndWriteTo(directory, List.of(quickMakeKnowledgeBaseWithContent(2, "New Directory", "New content")));
        indexModel.setDirectory(directory);

        assertEquals(2L, indexModel.searchByString("New").get(0).getId());
    }

    private KnowledgeBase quickMakeKnowledgeBaseWithContent(int id, String title, String content) {
        KnowledgeBaseContent contents = new KnowledgeBaseContent(id, "v1.0.0", content);
        return new KnowledgeBase.KnowledgeBaseBuilder()
                .setId(id)
                .setTitle(title)
                .setKnowledgeBaseContent(contents)
                .setTagLine("Example tagline")
                .setDateCreated(new Date(System.currentTimeMillis()))
                .build();
    }

}
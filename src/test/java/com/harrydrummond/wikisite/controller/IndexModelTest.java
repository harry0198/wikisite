package com.harrydrummond.wikisite.controller;

import com.harrydrummond.wikisite.entity.KnowledgeBase;
import com.harrydrummond.wikisite.entity.KnowledgeBaseContent;
import com.harrydrummond.wikisite.model.IndexModel;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class IndexModelTest {

    @Test
    public void givenSearchQueryWhenFetchedDocumentThenCorrect() {
        IndexModel controller = new IndexModel();
        KnowledgeBase kb = quickMakeKnowledgeBaseWithContent(1, "Example Title", "Searchable content for querying here! Hello World");
        controller.scanKnowledgeBaseToIndex(kb);

        List<KnowledgeBase> documents = controller.searchByString("lots and lots of random irrelevant things BOOM HELLO WORLD okay");
        assertEquals(
                1L,
                documents.get(0).getId());
    }

    @Test
    public void testSetDirectory() {
        IndexModel model = new IndexModel();

        model.scanKnowledgeBaseToIndex(quickMakeKnowledgeBaseWithContent(1,"Test Set Directory", "example search content"));

        assertEquals(1L, model.searchByString("example search lots and lots of otehr things").get(0).getId());

        Directory directory = new ByteBuffersDirectory();

        model.scanAndWriteTo(directory, List.of(quickMakeKnowledgeBaseWithContent(2, "New Directory", "New content")));
        model.setDirectory(directory);

        assertEquals(2L, model.searchByString("New").get(0).getId());
    }

    private KnowledgeBase quickMakeKnowledgeBase(int id, String title) {
        return quickMakeKnowledgeBaseWithContent(id, title, "Default content");
    }

    private KnowledgeBase quickMakeKnowledgeBaseWithContent(int id, String title, String content) {
        KnowledgeBaseContent contents = new KnowledgeBaseContent(id, "v1.0.0", content);
        return new KnowledgeBase(id, title, contents);
    }

}
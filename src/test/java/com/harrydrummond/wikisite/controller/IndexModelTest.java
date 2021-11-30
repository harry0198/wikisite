package com.harrydrummond.wikisite.controller;

import com.harrydrummond.wikisite.entity.KnowledgeBase;
import com.harrydrummond.wikisite.entity.KnowledgeBaseContent;
import com.harrydrummond.wikisite.model.IndexModel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class IndexModelTest {

    @Test
    public void givenSearchQueryWhenFetchedDocumentThenCorrect() {
        IndexModel controller = new IndexModel();
        KnowledgeBaseContent content = new KnowledgeBaseContent(1,"v1.0.0", "Searchable content for querying here! Hello World");
        KnowledgeBase kb = new KnowledgeBase(1,"Example Title",content);
        controller.scanKnowledgeBaseToIndex(kb);

        List<KnowledgeBase> documents = controller.searchByString("lots and lots of random irrelevant things BOOM HELLO WORLD okay");
        assertEquals(
                1L,
                documents.get(0).getId());
    }

}
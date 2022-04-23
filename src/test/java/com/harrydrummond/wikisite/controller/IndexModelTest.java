package com.harrydrummond.wikisite.controller;
//
//import com.harrydrummond.wikisite.articles.IndexModel;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.sql.Date;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//public class IndexModelTest {
//
//    @Autowired private IndexModel indexModel;
//
////    @Test
////    public void givenSearchQueryWhenFetchedDocumentThenCorrect() {
////        Article kb = quickMakeArticleWithContent(1, "Example Title", "Searchable content for querying here! Hello World");
////        indexModel.scanArticleToIndex(kb);
////
////        List<Article> documents = indexModel.searchByString("lots and lots of random irrelevant things BOOM HELLO WORLD okay");
////        assertEquals(
////                1L,
////                documents.get(0).getId());
////    }
////
////    @Test
////    public void testSetDirectory() {
////
////        indexModel.scanArticleToIndex(quickMakeKnowledgeBaseWithContent(1,"Test Set Directory", "example search content"));
////
////        assertEquals(1L, indexModel.searchByString("example search lots and lots of otehr things").get(0).getId());
////
////        Directory directory = new ByteBuffersDirectory();
////
////        indexModel.scanAndWriteTo(directory, List.of(quickMakeKnowledgeBaseWithContent(2, "New Directory", "New content")));
////        indexModel.setDirectory(directory);
////
////        assertEquals(2L, indexModel.searchByString("New").get(0).getId());
////    }
//
////    private Article quickMakeKnowledgeBaseWithContent(int id, String title, String content) {
////        ArticleContent contents = new ArticleContent(id, "v1.0.0", content);
////        return new Article.ArticleBuilder()
////                .id(id)
////                .title(title)
////                .knowledgeBaseContent(contents)
////                .tagLine("Example tagline")
////                .dateCreated(new Date(System.currentTimeMillis()))
////                .build();
////    }
//
//}
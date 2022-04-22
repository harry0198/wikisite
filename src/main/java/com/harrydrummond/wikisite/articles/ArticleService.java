package com.harrydrummond.wikisite.articles;

import com.harrydrummond.wikisite.articles.content.ArticleContentRepository;
import com.harrydrummond.wikisite.articles.search.ArticleSearcherService;
import com.harrydrummond.wikisite.articles.tags.Tag;
import com.harrydrummond.wikisite.articles.tags.TagRepository;
import com.harrydrummond.wikisite.util.DateUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class ArticleService {

    private final ArticleSearcherService articleSearcherService;
    private final ArticleRepository articleRepository;
    private final ArticleContentRepository articleContentRepository;
    private final TagRepository tagRepository;

    public Article loadArticleById(long id) {
        return articleRepository.findById(id).orElseThrow(() -> new IllegalStateException("Article does not exist with provided id"));
    }

    public List<Article> loadAllArticles() {
        return articleRepository.findAll();
    }

    public int getTotalViews() {
        return articleContentRepository.selectViews();
    }

    /**
     * Finds KnowledgeBase by title. If no KnowledgeBase by title exists, returns null.
     * @param title Title of KnowledgeBase to find
     * @return KnowledgeBase by title or null if not exists.
     */
    public Article findByTitle(String title) {
        //        addTags(knowledgeBase);
        return articleRepository.findByTitle(title);
    }

//    private void addTags(final Article article) {
//        Date curDate = new Date();
//        if (curDate.before(DateUtil.addDayToJavaUtilDate(article.getDateCreated(), 7))) {
//            Tag tag = new Tag("New", "Newly added articles.");
//            article.addTag(tag);
//        }
//    }
}
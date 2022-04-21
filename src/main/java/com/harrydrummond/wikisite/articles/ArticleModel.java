package com.harrydrummond.wikisite.articles;

import com.harrydrummond.wikisite.articles.content.ArticleContentRepository;
import com.harrydrummond.wikisite.util.DateUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * KnowledgeBase Model class and preferred access point
 */
@Component
@AllArgsConstructor
public final class ArticleModel {

    private final IndexModel indexModel;
    private final ArticleRepository kbRepository;
    private final TagRepository tagRepository;
    private final ArticleContentRepository kbContentRepository;

    /**
     * Gets all knowledge bases ordered by that defined in KnowledgeBase Comparable Interface.
     * Retrieves all data via data indexed by {@link IndexModel}
     * @return List of KnowledgeBases ordered by #compare
     */
    public List<Article> getAllArticlesFromIndex() {
        List<Article> kb = indexModel.getAllArticles();
        kb = kb.stream().sorted().collect(Collectors.toList());

        return kb;
    }

    /**
     * Finds knowledgebases by query via {@link IndexModel#searchByString(String)}
     * Retrieves all data via data indexed by {@link IndexModel}
     * @param query String to find knowledgebases by
     * @return List of knowledgebases found by query
     */
    public List<Article> findKnowledgeBasesByQueryFromIndex(String query) {
        return indexModel.searchByString(query);
    }

    public int getTotalViews() {
        return kbContentRepository.selectViews();
    }

    /**
     * Finds KnowledgeBase by title. If no KnowledgeBase by title exists, returns null.
     * @param title Title of KnowledgeBase to find
     * @return KnowledgeBase by title or null if not exists.
     */
    public Article findByTitle(String title) {
        Article knowledgeBase = kbRepository.findByTitle(title);
        if (knowledgeBase == null) return null;
        addTags(knowledgeBase);
        return knowledgeBase;
    }

    private void addTags(final Article knowledgeBase) {
        Date curDate = new Date();
        if (curDate.before(DateUtil.addDayToJavaUtilDate(knowledgeBase.getDateCreated(), 7))) {
            Optional<Tag> tag = tagRepository.findById("New");
            tag.ifPresent(knowledgeBase::addTag);
        }
    }
}
package com.harrydrummond.wikisite.articles.content;

import com.harrydrummond.wikisite.appuser.AppUser;
import com.harrydrummond.wikisite.articles.Article;
import com.harrydrummond.wikisite.parser.ParserUtil;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

/**
 * Entity class for KnowledgeBaseContent
 * Provides various getter/setter methods with conversion methods for readable format e.g content from CLOB to string
 */
@Entity
@Getter
@Setter
@Table(name = "kb_content")
public class ArticleContent {

    @Id
    @Column(name = "kb_content_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "version", nullable = false)
    private String versionString;

    @Column(name = "version_info")
    private String versionInfo;

    @Lob
    @Column(columnDefinition="CLOB", nullable = false)
    private String content;

    @Column(name = "date_created", nullable = false)
    private Date dateCreated;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kb_id", nullable = false)
    private Article knowledgeBase;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "app_user_id"
    )
    private AppUser appUser;

    @Column(name = "views")
    private Long views = 0L;

    /**
     * No arg constructor
     */
    public ArticleContent() {}

    /**
     * KnowledgeBaseContent custom constructor
     * Defaults views to 0
     * @param id ID to set
     * @param versionString Version of Content
     * @param content Actual content
     */
    public ArticleContent(long id, String versionString, String content) {
        this.id = id;
        this.versionString = versionString;
        this.content = content;
        this.views = 0L;
        this.versionInfo = "Default Release Info";
    }

    /**
     * Increments view count by 1
     */
    public void incrementViews() {
        views++;
    }

    /**
     * Gets author of content
     * @return Author of content
     */
    public String getAuthor() {
        return appUser.getUsername();
    }

    /**
     * Gets string-parsed content
     * @return String-parsed content
     */
    public String getContent() {
        return ParserUtil.parse(content);
    }

    /**
     * Gets the date content was created
     * @return Date created
     */
    public Date getDateCreated() {
        return dateCreated == null ? new Date(System.currentTimeMillis()) : dateCreated;
    }
}
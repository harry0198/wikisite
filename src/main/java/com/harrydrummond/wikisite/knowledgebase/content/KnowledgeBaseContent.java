package com.harrydrummond.wikisite.knowledgebase.content;

import com.harrydrummond.wikisite.knowledgebase.KnowledgeBase;
import com.harrydrummond.wikisite.parser.ParserUtil;

import javax.persistence.*;
import java.sql.Date;

/**
 * Entity class for KnowledgeBaseContent
 * Provides various getter/setter methods with conversion methods for readable format e.g content from CLOB to string
 */
@Entity
@Table(name = "kb_content")
public class KnowledgeBaseContent {

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
    private KnowledgeBase knowledgeBase;

    @Column(name = "app_user_id")
    private String author;

    @Column(name = "views")
    private Long views;

    /**
     * No arg constructor
     */
    public KnowledgeBaseContent() {}

    /**
     * KnowledgeBaseContent custom constructor
     * Defaults views to 0
     * @param id ID to set
     * @param versionString Version of Content
     * @param content Actual content
     */
    public KnowledgeBaseContent(long id, String versionString, String content) {
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
        return author;
    }

    /**
     * Gets ID of content
     * @return ID of content
     */
    public Long getId() {
        return id;
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

    /**
     * Gets version of content
     * @return Version
     */
    public String getVersionString() {
        return versionString;
    }

    /**
     * Gets version information
     * @return Version information
     */
    public String getVersionInfo() {
        return versionInfo;
    }

    /**
     * Sets the ID
     * @param id ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Sets version
     * @param versionString Version to set
     */
    public void setVersionString(String versionString) {
        this.versionString = versionString;
    }

    /**
     * Sets content
     * @param content Content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Sets the date created
     * @param dateCreated Date to set
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
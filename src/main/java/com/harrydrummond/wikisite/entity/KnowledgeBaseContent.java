package com.harrydrummond.wikisite.entity;

import com.harrydrummond.wikisite.parser.ParserUtil;

import javax.persistence.*;
import java.sql.Date;

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

    @Column(name = "views")
    private Long views;

    public void incrementViews() {
        views++;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return ParserUtil.parse(content);
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersionString() {
        return versionString;
    }

    public void setVersionString(String versionString) {
        this.versionString = versionString;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVersionInfo() {
        return versionInfo;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
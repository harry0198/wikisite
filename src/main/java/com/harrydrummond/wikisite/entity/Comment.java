package com.harrydrummond.wikisite.entity;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "content", nullable = false)
    private String text;

    @Column(name = "date_posted", nullable = false)
    private Date datePosted;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kb_id", nullable = false)
    private KnowledgeBase knowledgeBase;

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Date getDatePosted() {
        return datePosted;
    }

    public KnowledgeBase getKnowledgeBase() {
        return knowledgeBase;
    }

    //todo link username
}
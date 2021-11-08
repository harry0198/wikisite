package com.harrydrummond.wikisite.entity;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "kb_content")
public class KnowledgeBaseContent {

    @Id
    @Column(name = "kb_content_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    @Column(columnDefinition="BLOB", nullable = false)
    private byte[] content;

    @Column(name = "date_created", nullable = false)
    private Date dateCreated;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kb_id", nullable = false)
    private KnowledgeBase knowledgeBase;


}
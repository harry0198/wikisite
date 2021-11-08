package com.harrydrummond.wikisite.entity;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Entity
@Table(name = "kb")
public class KnowledgeBase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "kb_id")
    private Integer id;

    @OneToMany(mappedBy = "knowledgeBase", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Column(name = "content")
    private Set<KnowledgeBaseContent> possibleContents;

    @Column(unique = true, nullable = false)
    private String title;

    @Column(name = "date_created", nullable = false)
    private Date dateCreated;

    @ManyToMany
    @JoinTable(name = "kb_tag_relation",
    joinColumns = @JoinColumn(name = "kb_id"),
    inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;

    @OneToMany(mappedBy = "knowledgeBase", fetch = FetchType.LAZY)
    @Column(name = "content")
    private Set<Comment> comments;

    public Integer getId() {
        return id;
    }

    public Set<KnowledgeBaseContent> getPossibleContents() {
        return possibleContents;
    }

    public String getTitle() {
        return title;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public Set<Comment> getComments() {
        return comments;
    }
}
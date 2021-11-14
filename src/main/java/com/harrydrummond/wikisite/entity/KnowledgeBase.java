package com.harrydrummond.wikisite.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Column(name = "tag_line")
    private String tagLine = "";

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Column(name = "date_created", nullable = false)
    private Date dateCreated;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "kb_tag_relation",
    joinColumns = @JoinColumn(name = "kb_id"),
    inverseJoinColumns = @JoinColumn(name = "tag_name"))
    private Set<Tag> tags;

    @OneToMany(mappedBy = "knowledgeBase", fetch = FetchType.LAZY)
    @Column(name = "content")
    private Set<Comment> comments;

    @Column(nullable = false)
    private int rating;

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

    public KnowledgeBaseContent getLatestKnowledgeBaseContent() {
        List<KnowledgeBaseContent> contents = getPossibleContents().stream().sorted(Comparator.comparing(KnowledgeBaseContent::getDateCreated)).collect(Collectors.toList());
        if (contents.isEmpty()) {
            System.out.println("Empty!");
            KnowledgeBaseContent tmpContent = new KnowledgeBaseContent();
            tmpContent.setId(-1L);
            tmpContent.setContent("<h1>Nothing Here! :(</h1><p>Something went wrong! Please contact an administrator. </p>");
            tmpContent.setDateCreated(new Date(0));
            tmpContent.setVersionString("v0.0.0");
            return tmpContent;
        }
        System.out.println("we out here");
        return contents.stream().findFirst().get();
    }

    public KnowledgeBaseContent getKnowledgeBaseContentFromVersion(String version) {
        List<KnowledgeBaseContent> contents = getPossibleContents().stream().filter(t -> t.getVersionString().equalsIgnoreCase(version)).collect(Collectors.toList());
        if (contents.isEmpty()) {
            return null;
        }
        return contents.stream().findFirst().get();
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public int getRating() {
        return rating;
    }



    public Set<Comment> getComments() {
        return comments;
    }

    public String getTagLine() {
        return tagLine;
    }
}
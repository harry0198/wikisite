package com.harrydrummond.wikisite.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "kb")
public class KnowledgeBase implements Comparable<KnowledgeBase> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "kb_id")
    private Integer id;

    @OneToMany(mappedBy = "knowledgeBase", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Column(name = "content")
    @OrderBy("versionString DESC")
    private List<KnowledgeBaseContent> possibleContents;

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

    public List<KnowledgeBaseContent> getPossibleContents() {

        return possibleContents;
    }

    public String getTitle() {
        return title;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * Gets the knowledgebase content found by most recently created. If no content is found,
     * default KnowledgeBaseContent is returned with an id of -1L
     * @return Latest KnowledgeBaseContent related to this knowledgebase
     */
    public KnowledgeBaseContent getLatestKnowledgeBaseContent() {
        List<KnowledgeBaseContent> contents = getPossibleContents().stream().sorted(Comparator.comparing(KnowledgeBaseContent::getDateCreated)).collect(Collectors.toList());
        if (contents.isEmpty()) {
            KnowledgeBaseContent tmpContent = new KnowledgeBaseContent();
            tmpContent.setId(-1L);
            tmpContent.setContent("#Nothing Here! :(    Something went wrong! Please contact an administrator.");
            tmpContent.setDateCreated(new Date(0));
            tmpContent.setVersionString("v0.0.0");
            return tmpContent;
        }
        return contents.stream().findFirst().get();
    }

    /**
     * Gets the knowledgebasecontent from the version string provided. If
     * contents is empty, returns null
     * @param version Version string to find
     * @return KnowledgeBaseContent from version string, if none was found, returns null.
     */
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

    /**
     * Comparable interface implementation, compares by rating, if rating is
     * equal, date created is compared
     * @param o KnowledgeBase to compare
     * @return index of compared objects as outlined in compareTo. 0 if rating and date created are identical.
     */
    @Override
    public int compareTo(KnowledgeBase o) {
        int index = Integer.compare(o.getRating(), getRating());
        if (index == 0) {
            index = getDateCreated().compareTo(o.getDateCreated());
        }
        return index;
    }
}
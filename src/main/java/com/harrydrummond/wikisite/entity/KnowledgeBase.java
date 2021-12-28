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
    private Long id;

    @OneToMany(mappedBy = "knowledgeBase", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
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

    /**
     * No arg constructor
     */
    public KnowledgeBase() {}

    /**
     * KnowledgeBase quick creation
     * @param id Id of KnowledgeBase
     * @param title Title of KnowledgeBase
     */
    public KnowledgeBase(long id, String title) {
        this.id = id;
        this.title = title;
    }

    /**
     * Initialises KnowledgeBase with ID, Title and content. Primarily used for testing purposes.
     * Sets rating to 0 and sets date created to today.
     * @param id Integer ID
     * @param title String title
     * @param content Knowledgebase content
     */
    public KnowledgeBase(long id, String title, KnowledgeBaseContent content) {
        this(id, title);
        this.dateCreated = new Date(System.currentTimeMillis());
        this.possibleContents = new ArrayList<>();
        this.possibleContents.add(content);
        this.rating = 0;
    }

    /**
     * Adds tag to tag list
     * @param tag Tag to add
     */
    public void addTag(Tag tag) {
        tags.add(tag);
    }

    /**
     * Gets the knowledgebase content found by most recently created. If no content is found,
     * default KnowledgeBaseContent is returned with an id of -1L
     * @return Latest KnowledgeBaseContent related to this knowledgebase
     */
    public KnowledgeBaseContent getLatestKnowledgeBaseContent() {
        List<KnowledgeBaseContent> contents = getPossibleContents()
                .stream()
                .sorted(Comparator.comparing(KnowledgeBaseContent::getDateCreated))
                .collect(Collectors.toList());
        Collections.reverse(contents);
        if (contents.isEmpty()) {
            KnowledgeBaseContent tmpContent = new KnowledgeBaseContent(-1,"v0.0.0", "#Nothing Here! :(    Something went wrong! Please contact an administrator.");
            tmpContent.setDateCreated(new Date(0));
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

    /**
     * Gets KnowledgeBase numerical ID
     * @return Gets ID of KnowledgeBase
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets a list of possible contents the KnowledgeBase may have. Such as previous versions
     * @return List of KnowledgeBaseContents
     */
    public List<KnowledgeBaseContent> getPossibleContents() {
        return possibleContents;
    }

    /**
     * Gets title of KnowledgeBase
     * @return Title of KnowledgeBase
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets Date KnowledgeBase was created
     * @return Date KnowledgeBase was created
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * Gets Tags applied to KnowledgeBase
     * @return Set of tags applied to KnowledgeBase
     */
    public Set<Tag> getTags() {
        return tags;
    }

    /**
     * Gets rating of KnowledgeBase
     * @return Numerical valued Rating
     */
    public int getRating() {
        return rating;
    }

    /**
     * Get all comments relating to KnowledgeBase
     * @return Set of Comments
     */
    public Set<Comment> getComments() {
        return comments;
    }

    /**
     * Gets the tagline of KnowledgeBase
     * @return Tagline of KnowledgeBase
     */
    public String getTagLine() {
        return tagLine;
    }

    /**
     * Gets a single-stringed KnowledgeBase title to be used for a URL
     * @return Single-stringed title for URL usage
     */
    public String getKnowledgeBaseUrlSafe() {
        return title.replaceAll(" ", "-");
    }

    /**
     * Gets Date latest content was updated
     * @return Date of latest content
     */
    public Date getLatestContentUpdate() {
        KnowledgeBaseContent content = getLatestKnowledgeBaseContent();
        if (content.getId() != -1) {
            return content.getDateCreated();
        }
        return dateCreated;
    }

    /**
     * Sets the rating of KnowledgeBase
     * @param rating Rating to set
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Sets Date created
     * @param dateCreated Date to set
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * Sets tagline
     * @param tagLine Tagline to set
     */
    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
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
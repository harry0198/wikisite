package com.harrydrummond.wikisite.knowledgebase;

import com.harrydrummond.wikisite.knowledgebase.content.KnowledgeBaseContent;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "kb")
public class KnowledgeBase implements Comparable<KnowledgeBase> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "kb_id", nullable = false)
    private Long id;

    @OneToMany(mappedBy = "knowledgeBase", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Column(name = "content")
    @OrderBy("versionString DESC")
    private @Nullable List<KnowledgeBaseContent> possibleContents;

    @Column(unique = true, nullable = false)
    private String title;

    @Column(name = "tag_line", nullable = false)
    private String tagLine = "";

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Column(name = "date_created", nullable = false)
    private Date dateCreated;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "kb_tag_relation", joinColumns = @JoinColumn(name = "kb_id"), inverseJoinColumns = @JoinColumn(name = "tag_name"))
    private @Nullable Set<Tag> tags;

    @Column(nullable = false)
    private int rating;

    /**
     * No arg constructor
     */
    public KnowledgeBase() {}

    // Creates object via KnowledgeBaseBuilder
    private KnowledgeBase(KnowledgeBaseBuilder kbb) {
        this.id = kbb.id;
        this.tagLine = kbb.tagLine;
        this.title = kbb.title;
        this.dateCreated = kbb.dateCreated;
        this.rating = kbb.rating;
        if (kbb.knowledgeBaseContent != null) {
            this.possibleContents = new ArrayList<>();
            this.possibleContents.add(kbb.knowledgeBaseContent);
        }
    }

    /**
     * Adds tag to tag list
     * @param tag Tag to add
     */
    public void addTag(Tag tag) {
        if (tags == null)
            tags = Set.of(tag);
        else
            tags.add(tag);
    }

    /*
        GETTERS
     */

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
     * Gets a list of possible contents the KnowledgeBase may have. Such as previous versions.
     * @return List of KnowledgeBaseContents or empty list if contents is null
     */
    public @NonNull List<KnowledgeBaseContent> getPossibleContents() {
        return possibleContents == null ? new ArrayList<>() : possibleContents;
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

    /*
        SETTERS
     */

    /**
     * Sets the rating of KnowledgeBase
     * @param rating Rating to set
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Sets title of the article
     * @param title Title to set
     */
    public void setTitle(String title) {
        this.title = title;
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

    @Override
    public String toString() {
        return "KnowledgeBase{" +
                "id=" + id +
                ", possibleContents=" + possibleContents +
                ", title='" + title + '\'' +
                ", tagLine='" + tagLine + '\'' +
                ", dateCreated=" + dateCreated +
                ", tags=" + tags +
                ", rating=" + rating +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KnowledgeBase that = (KnowledgeBase) o;
        return rating == that.rating && id.equals(that.id) && Objects.equals(possibleContents, that.possibleContents) && title.equals(that.title) && tagLine.equals(that.tagLine) && dateCreated.equals(that.dateCreated) && Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, possibleContents, title, tagLine, dateCreated, tags, rating);
    }

    public static class KnowledgeBaseBuilder {

        private long id;
        private String title;
        private String tagLine;
        private Date dateCreated;
        private int rating;
        private KnowledgeBaseContent knowledgeBaseContent;

        public KnowledgeBaseBuilder setId(long id) {
            this.id = id;
            return this;
        }

        public KnowledgeBaseBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public KnowledgeBaseBuilder setTagLine(String tagLine) {
            this.tagLine = tagLine;
            return this;
        }

        public KnowledgeBaseBuilder setDateCreated(Date dateCreated) {
            this.dateCreated = dateCreated;
            return this;
        }

        public KnowledgeBaseBuilder setRating(int rating) {
            this.rating = rating;
            return this;
        }

        public KnowledgeBaseBuilder setKnowledgeBaseContent(KnowledgeBaseContent knowledgeBaseContent) {
            this.knowledgeBaseContent = knowledgeBaseContent;
            return this;
        }

        public KnowledgeBase build() {
            KnowledgeBase kb = new KnowledgeBase(this);
            validate(kb);
            return kb;
        }

        // Validates the created KnowledgeBase object. Checks if there are any missing assumptions before
        // returning object to user.
        private void validate(KnowledgeBase kb) {
            assert kb.id != null;
            assert kb.dateCreated != null;
            assert kb.title != null;
            assert kb.tagLine != null;
        }

    }
}
package com.harrydrummond.wikisite.articles;

import com.harrydrummond.wikisite.articles.content.ArticleContent;
import com.harrydrummond.wikisite.articles.tags.Tag;
import lombok.*;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "kb")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Indexed
@EqualsAndHashCode
@ToString
@Builder(access = AccessLevel.PUBLIC)
public class Article implements Comparable<Article> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kb_id", nullable = false)
    private Long id;

    @OneToMany(mappedBy = "knowledgeBase", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Column(name = "content")
    @OrderBy("versionString DESC")
    @IndexedEmbedded
    private @Nullable List<ArticleContent> possibleContents = new ArrayList<>();

    @Column(unique = true)
    @NonNull
    @FullTextField
    private String title;

    @Column(name = "tag_line")
    @NonNull
    private String tagLine;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @NonNull
    @GenericField(sortable = Sortable.YES)
    @Column(name = "date_created")
    private Date dateCreated;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "kb_tag_relation", joinColumns = @JoinColumn(name = "kb_id"), inverseJoinColumns = @JoinColumn(name = "tag_name"))
    @IndexedEmbedded
    private @Nullable Set<Tag> tags;

    @Nullable
    private int rating = 5;

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
    public ArticleContent getLatestArticleContent() {
        List<ArticleContent> contents = getPossibleContents()
                .stream()
                .sorted(Comparator.comparing(ArticleContent::getDateCreated))
                .collect(Collectors.toList());
        if (contents.isEmpty()) {
            ArticleContent tmpContent = new ArticleContent(-1,"v0.0.0", "#Nothing Here! :(    Something went wrong! Please contact an administrator.");
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
    public ArticleContent getArticleContentFromVersion(String version) {
        List<ArticleContent> contents = getPossibleContents().stream().filter(t -> t.getVersionString().equalsIgnoreCase(version)).collect(Collectors.toList());
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
    public @NonNull List<ArticleContent> getPossibleContents() {
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
    public String getArticleUrlSafe() {
        return title.replaceAll(" ", "-");
    }

    /**
     * Gets Date latest content was updated
     * @return Date of latest content
     */
    public Date getLatestArticleUpdate() {
        ArticleContent content = getLatestArticleContent();
        if (content.getId() != -1) {
            return content.getDateCreated();
        }
        return dateCreated;
    }

    /*
        SETTERS
     */

    /**
     * Comparable interface implementation, compares by rating, if rating is
     * equal, date created is compared
     * @param o KnowledgeBase to compare
     * @return index of compared objects as outlined in compareTo. 0 if rating and date created are identical.
     */
    @Override
    public int compareTo(Article o) {
        int index = Integer.compare(o.getRating(), getRating());
        if (index == 0) {
            index = getDateCreated().compareTo(o.getDateCreated());
        }
        return index;
    }
}
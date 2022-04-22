package com.harrydrummond.wikisite.articles.tags;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.harrydrummond.wikisite.articles.Article;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "tag")
public class Tag implements Serializable {

    @Id
    @Column(nullable = false, name = "tag_name")
    @NonNull
    @KeywordField
    private String name;

    // Default @column nullable is true
    @Nullable
    private String description;

    @ManyToMany(mappedBy = "tags")
    Set<Article> taggedKnowledgeBases;

    public Tag() {}

    public Tag(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    /**
     * Gets the search ready name, replaces all spaces in tag names with '-'
     * @return Search ready name
     */
    public String getSearchReadyName() {
        return name.replaceAll(" ", "-");
    }

    public String getDescription() {
        return description;
    }

    @JsonIgnore
    public Set<Article> getTaggedKnowledgeBases() {
        return taggedKnowledgeBases;
    }
}
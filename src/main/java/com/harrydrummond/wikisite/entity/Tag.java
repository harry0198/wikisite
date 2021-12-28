package com.harrydrummond.wikisite.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "tag")
public class Tag implements Serializable {

    @Id
    @Column(nullable = false, name = "tag_name")
    private String name;

    // Default @column nullable is true
    private String description;

    @ManyToMany(mappedBy = "tags")
    Set<KnowledgeBase> taggedKnowledgeBases;

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
    public Set<KnowledgeBase> getTaggedKnowledgeBases() {
        return taggedKnowledgeBases;
    }
}
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

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @JsonIgnore
    public Set<KnowledgeBase> getTaggedKnowledgeBases() {
        return taggedKnowledgeBases;
    }
}
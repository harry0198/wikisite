package com.harrydrummond.wikisite.posts;

import com.harrydrummond.wikisite.appuser.AppUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Indexed
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @FullTextField
    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "post")
    @IndexedEmbedded
    private Set<Image> images = new HashSet<>();

    @FullTextField
    @Lob
    @Column(columnDefinition="CLOB", nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(nullable = false, name = "author_id")
    private AppUser poster;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @GenericField(sortable = Sortable.YES)
    @Column(name = "posted_at", nullable = false)
    private Date datePosted;

    public String getTitleUrlSafe() {
        return title.toLowerCase().replaceAll(" ", "-");
    }

}
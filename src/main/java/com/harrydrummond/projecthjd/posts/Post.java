package com.harrydrummond.projecthjd.posts;

import com.harrydrummond.projecthjd.posts.image.Image;
import com.harrydrummond.projecthjd.user.User;
import com.harrydrummond.projecthjd.user.likes.UserLikes;
import com.harrydrummond.projecthjd.user.saves.UserSaves;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
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

    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    @OneToMany(mappedBy = "post")
    @IndexedEmbedded
    private Set<Image> images = new HashSet<>();

    @FullTextField
    @Lob
    @Column(columnDefinition="CLOB", nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(nullable = false, name = "author_id")
    private User poster;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @GenericField(sortable = Sortable.YES)
    @Column(name = "posted_at", nullable = false)
    private Date datePosted;

    @OneToMany(mappedBy = "user")
    private Set<UserLikes> likes = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<UserSaves> saves = new HashSet<>();


    public String getTitleUrlSafe() {
        return title.toLowerCase().replaceAll(" ", "-");
    }

}
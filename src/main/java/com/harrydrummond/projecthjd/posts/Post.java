package com.harrydrummond.projecthjd.posts;

import com.harrydrummond.projecthjd.posts.comment.Comment;
import com.harrydrummond.projecthjd.posts.image.Image;
import com.harrydrummond.projecthjd.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private Set<Image> images = new HashSet<>();

    @FullTextField
    @Lob
    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(nullable = false, name = "author_id")
    private User poster;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @GenericField(sortable = Sortable.YES)
    @Column(name = "posted_at", nullable = false)
    private LocalDateTime datePosted;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @GenericField(sortable = Sortable.YES)
    private long views = 0L;

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public void removeCommentById(long id) {
        comments.stream().filter(x -> x.getId() == id).findAny().ifPresent(comment -> comments.remove(comment));
    }

    public String getMainImagePath() {
        Optional<Image> image = images.stream().findFirst();
        if (image.isPresent()) return image.get().getPath();
        return "";
    }

    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return datePosted != null ? datePosted.format(formatter) : "00/00/0000";

    }

    public void incViews() {
        views++;
    }
}
package com.harrydrummond.projecthjd.posts.image;


import com.harrydrummond.projecthjd.posts.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Image {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name="post_id", nullable=false)
    private Post post;

    @Column
    private String path;

    @Column
    @FullTextField
    private String alt;

    @Column(name = "order_no")
    @NonNull
    private int orderNo;
}
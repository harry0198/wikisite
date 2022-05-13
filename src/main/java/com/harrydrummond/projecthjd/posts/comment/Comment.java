package com.harrydrummond.projecthjd.posts.comment;

import com.harrydrummond.projecthjd.posts.Post;
import com.harrydrummond.projecthjd.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@Entity
@Table
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String comment;

    @ManyToOne
    @JoinColumn(name ="post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name ="user_id")
    private User user;

    private LocalDateTime datePosted;

    @OneToMany
    @JoinColumn(name ="parent_id")
    private List<Comment> replies;

    public void addReply(Comment comment) {
        if (replies == null) {
            replies = new ArrayList<>();
        }
        replies.add(comment);
    }
}
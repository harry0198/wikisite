package com.harrydrummond.projecthjd.posts.comment;

import com.harrydrummond.projecthjd.posts.Post;
import com.harrydrummond.projecthjd.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
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

    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return datePosted != null ? datePosted.format(formatter) : "00/00/0000";
    }
}
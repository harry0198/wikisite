package com.harrydrummond.wikisite.appuser.likes;

import com.harrydrummond.wikisite.appuser.User;
import com.harrydrummond.wikisite.posts.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "user_likes")
public class UserLikes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    public UserLikes(Post post, User user) {
        this.post = post;
        this.user = user;
    }
}
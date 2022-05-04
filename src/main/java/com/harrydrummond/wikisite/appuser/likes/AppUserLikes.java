package com.harrydrummond.wikisite.appuser.likes;

import com.harrydrummond.wikisite.appuser.AppUser;
import com.harrydrummond.wikisite.posts.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "user_likes")
public class AppUserLikes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private AppUser appUser;

    public AppUserLikes(Post post, AppUser appUser) {
        this.post = post;
        this.appUser = appUser;
    }
}
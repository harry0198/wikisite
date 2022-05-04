package com.harrydrummond.wikisite.appuser.saves;

import com.harrydrummond.wikisite.appuser.AppUser;
import com.harrydrummond.wikisite.posts.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "user_saves")
public class AppUserSaves {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private AppUser appUser;

    public AppUserSaves(Post post, AppUser appUser) {
        this.post = post;
        this.appUser = appUser;
    }

}
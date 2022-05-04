package com.harrydrummond.wikisite.appuser;


import com.harrydrummond.wikisite.appuser.likes.AppUserLikes;
import com.harrydrummond.wikisite.appuser.saves.AppUserSaves;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "app_user")
public class AppUser implements OAuth2User, Serializable {

    private static final List<GrantedAuthority> ROLE_USER = Collections
            .unmodifiableList(AuthorityUtils.createAuthorityList("USER"));

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username")
    private String name;
    private String email;
    @Enumerated(EnumType.STRING)
    @Column(name = "app_user_role")
    private AppUserRole appUserRole;
    private Boolean locked = false;
    private Boolean enabled = false;
    @Column(name = "date_created")
    private LocalDateTime dateCreated;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @OneToMany(mappedBy="post", fetch = FetchType.EAGER)
    private Set<AppUserLikes> likes = new HashSet<>();

    @OneToMany(mappedBy="post", fetch = FetchType.EAGER)
    private Set<AppUserSaves> saves = new HashSet<>();

    private transient Map<String, Object> attributes;

    public AppUser(String name, String email, AppUserRole appUserRole) {
        this.name = name;
        this.email = email;
        this.appUserRole = appUserRole;
        this.dateCreated = LocalDateTime.now();
    }

    public AppUser(AppUser appUser) {
        this.id = appUser.id;
        this.email = appUser.email;
        this.name = appUser.name;
        this.appUserRole = appUser.appUserRole;
        this.locked = appUser.locked;
        this.enabled = appUser.enabled;
        this.dateCreated = appUser.dateCreated;
        this.provider = appUser.provider;
        this.likes = appUser.likes;
        this.saves = appUser.saves;
    }

    public void addLike(AppUserLikes appUserLikes) {
        likes.add(appUserLikes);
    }

    public void removeLike(AppUserLikes appUserLikes) {
        likes.remove(appUserLikes);
    }

    public boolean isLiked(long articleId) {
        for (AppUserLikes save : likes) {
            long article = save.getPost().getId();
            if (article == articleId) return true;
        }
        return false;
    }

    public void addSave(AppUserSaves appUserSaves) {
        saves.add(appUserSaves);
    }

    public void removeSave(AppUserSaves appUserSaves) {
        saves.remove(appUserSaves);
    }

    public boolean isSaved(long articleId) {
        for (AppUserSaves save : saves) {
            long article = save.getPost().getId();
            if (article == articleId) return true;
        }
        return false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return ROLE_USER;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AppUser appUser = (AppUser) o;
        return id != null && Objects.equals(id, appUser.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
package com.harrydrummond.wikisite.appuser;


import com.harrydrummond.wikisite.appuser.likes.UserLikes;
import com.harrydrummond.wikisite.appuser.saves.UserSaves;
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
public class User implements OAuth2User, Serializable {

    private static final List<GrantedAuthority> ROLE_USER = Collections
            .unmodifiableList(AuthorityUtils.createAuthorityList("USER"));

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username")
    private String username;
    private String email;
    @Enumerated(EnumType.STRING)
    @Column(name = "app_user_role")
    private UserRole userRole;
    private Boolean locked = false;
    private Boolean enabled = false;
    @Column(name = "date_created")
    private LocalDateTime dateCreated;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @OneToMany(mappedBy="post", cascade = CascadeType.ALL)
    private Set<UserLikes> likes = new HashSet<>();

    @OneToMany(mappedBy="post", cascade = CascadeType.ALL)
    private Set<UserSaves> saves = new HashSet<>();

    private transient Map<String, Object> attributes;

    public User(String name, String email, UserRole userRole) {
        this.username = name;
        this.email = email;
        this.userRole = userRole;
        this.dateCreated = LocalDateTime.now();
    }

    public User(User user) {
        this.id = user.id;
        this.email = user.email;
        this.username = user.username;
        this.userRole = user.userRole;
        this.locked = user.locked;
        this.enabled = user.enabled;
        this.dateCreated = user.dateCreated;
        this.provider = user.provider;
        this.likes = user.likes;
        this.saves = user.saves;
    }

    public String getName() {
        return username;
    }

    public void addLike(UserLikes userLikes) {
        likes.add(userLikes);
    }

    public void removeLike(UserLikes userLikes) {
        likes.remove(userLikes);
    }

    public boolean isLiked(long articleId) {
        for (UserLikes save : likes) {
            long article = save.getPost().getId();
            if (article == articleId) return true;
        }
        return false;
    }

    public void addSave(UserSaves userSaves) {
        saves.add(userSaves);
    }

    public void removeSave(UserSaves userSaves) {
        saves.remove(userSaves);
    }

    public boolean isSaved(long articleId) {
        for (UserSaves save : saves) {
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
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
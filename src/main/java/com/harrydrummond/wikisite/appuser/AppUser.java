package com.harrydrummond.wikisite.appuser;


import com.harrydrummond.wikisite.articles.Article;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@EqualsAndHashCode
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

    @ManyToMany(mappedBy = "likes")
    private List<Article> likedArticles;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_saves", joinColumns = @JoinColumn(name = "kb_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<Article> savedArticles;

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
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return ROLE_USER;
    }
}
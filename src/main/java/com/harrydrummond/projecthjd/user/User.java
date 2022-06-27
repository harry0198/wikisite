package com.harrydrummond.projecthjd.user;


import com.harrydrummond.projecthjd.posts.Post;
import com.harrydrummond.projecthjd.user.details.UserDetails;
import com.harrydrummond.projecthjd.user.preferences.Preference;
import com.harrydrummond.projecthjd.user.preferences.Preferences;
import com.harrydrummond.projecthjd.user.roles.Role;
import com.harrydrummond.projecthjd.user.roles.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "app_user", uniqueConstraints = @UniqueConstraint(columnNames={"provider", "auth_id"}))
public class User implements OAuth2User, Serializable {

    private static final List<GrantedAuthority> ROLE_USER = Collections
            .unmodifiableList(AuthorityUtils.createAuthorityList("USER"));

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    @FullTextField
    private String username;
    @Email
    private String email;
    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER)
    private Set<UserRole> userRoles = new HashSet<>();
    private Boolean locked = false;
    private Boolean enabled = false;
    @Column(name = "date_created")
    private LocalDateTime dateCreated;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private Provider provider;

    @Column(name = "auth_id")
    private String authId;


    private transient Map<String, Object> attributes;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    @PrimaryKeyJoinColumn
    private UserDetails userDetails;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_preference")
    @OneToMany(mappedBy = "preference", fetch = FetchType.EAGER)
    private Set<Preferences> userPreferences = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "poster")
    private Set<Post> posts = new HashSet<>();

    public User(String name, String email, UserRole userRoles) {
        this.username = name;
        this.email = email;
        this.userRoles = Set.of(userRoles);
        this.dateCreated = LocalDateTime.now();
    }

    public User(User user) {
        this.id = user.id;
        this.email = user.email;
        this.username = user.username;
        this.userRoles = user.userRoles;
        this.locked = user.locked;
        this.enabled = user.enabled;
        this.dateCreated = user.dateCreated;
        this.provider = user.provider;
        this.userDetails = user.userDetails;
        this.userPreferences = user.userPreferences;
    }

    public boolean containsRole(Role role) {
        return userRoles.stream().map(UserRole::getRole).anyMatch(x -> x == role);
    }

    public Set<Preferences> getUserPreferences() {
        return userPreferences;
    }

    public boolean containsPreference(Preference preference) {
        return userPreferences.stream().map(Preferences::getPreference).anyMatch(x -> x == preference);
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return username;
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
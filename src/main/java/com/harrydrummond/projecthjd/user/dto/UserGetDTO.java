package com.harrydrummond.projecthjd.user.dto;

import com.harrydrummond.projecthjd.user.User;
import com.harrydrummond.projecthjd.user.roles.UserRole;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UserGetDTO {

    private String username;
    private String email;
    private Set<UserRole> userRoles;
    private boolean locked;
    private boolean enabled;
    private String bio;
    private LocalDateTime dateCreated;
    private String profilePicturePath;

    public UserGetDTO(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.userRoles = user.getUserRoles();
        this.locked = user.getLocked();
        this.enabled = user.getEnabled();
        this.bio = user.getUserDetails().getBio();
        this.dateCreated = user.getDateCreated();
        this.profilePicturePath = user.getUserDetails().getProfilePicturePath();
    }

}
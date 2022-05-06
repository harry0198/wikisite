package com.harrydrummond.projecthjd.user;

import com.harrydrummond.projecthjd.user.roles.UserRole;
import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {

    private String username;
    private String email;
    private Set<UserRole> userRoles;
    private boolean locked;
    private boolean enabled;
}
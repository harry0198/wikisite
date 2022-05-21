package com.harrydrummond.projecthjd.user.dto;

import com.harrydrummond.projecthjd.user.roles.UserRole;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Data
public class UserDTO {

    private String username;
    private String email;
    private Set<UserRole> userRoles;
    private Boolean locked;
    private Boolean enabled;
    private String bio;
    private MultipartFile profilePicture;
}
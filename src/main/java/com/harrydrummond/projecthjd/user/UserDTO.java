package com.harrydrummond.projecthjd.user;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {

    private final String username;
    private final String email;
    private final UserRole userRole;
    private final boolean locked;
    private final boolean enabled;
}
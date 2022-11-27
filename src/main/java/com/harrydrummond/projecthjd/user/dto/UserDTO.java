package com.harrydrummond.projecthjd.user.dto;

import com.harrydrummond.projecthjd.user.roles.UserRole;
import com.harrydrummond.projecthjd.validators.image.ImageConstraint;
import com.harrydrummond.projecthjd.validators.LinkConstraint;
import com.harrydrummond.projecthjd.validators.UniqueUsernameConstraint;
import com.harrydrummond.projecthjd.validators.image.SingleImageConstraint;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import java.util.Set;

@Data
public class UserDTO {

    @Length(min = 3, message = "{validation.username.too_short}")
    @Length(max = 20, message = "{validation.username.too_long}")
    @UniqueUsernameConstraint
    private String username;
    @Length(max = 100, message = "Too many characters!")
    @Email(message = "Email provided is an invalid format")
    private String email;
    private Set<UserRole> userRoles;
    @LinkConstraint
    @Length(max = 200, message = "Too many characters!")
    private String link;
    private Boolean locked;
    private Boolean enabled;
    @Length(max = 1500, message = "{validation.bio.too_long}")
    private String bio;
    @SingleImageConstraint
    private MultipartFile profilePicture;
}
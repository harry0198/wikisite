package com.harrydrummond.projecthjd.user;

import com.harrydrummond.projecthjd.filestorage.FileStorageService;
import com.harrydrummond.projecthjd.posts.image.ImageService;
import com.harrydrummond.projecthjd.user.dto.UserDTO;
import com.harrydrummond.projecthjd.user.dto.UserGetDTO;
import com.harrydrummond.projecthjd.user.roles.Role;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Controller
public class UserController {

    private final UserService userService;
    private final FileStorageService fileStorageService;

    /**
     * Gets the user defined in the path
     * @param uid ID Of the user
     * @return ResponseEntity with user details and http status. Returns NOT FOUND if user with id is not found.
     */
    @GetMapping("/api/user/{uid}")
    public @ResponseBody ResponseEntity<UserGetDTO> getUser(@PathVariable long uid) {
        Optional<User> user = userService.getUserById(uid);
        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new UserGetDTO(user.get()), HttpStatus.OK);
    }

    @PostMapping(value = "/api/user/update",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String updateUserForm(@AuthenticationPrincipal User requestingUser, UserDTO userDTO) {
        System.out.println("jee");
        updateUserFields(requestingUser, requestingUser, userDTO);

        return "redirect:/search";
    }

        /**
         * Updates user details
         * @param uid ID of user
         * @param userDTO User data transfer object containing values to update
         * @return ResponseEntity of updated values and http status. Returns NOT FOUND if user with id is not found.
         */
    @PostMapping(value = "/api/user/{uid}", consumes = "application/json")
    public @ResponseBody ResponseEntity<UserGetDTO> updateUser(@AuthenticationPrincipal User requestingUser, @PathVariable long uid, @RequestBody UserDTO userDTO) {
        // If user is not the same user as requesting to update and user is not an admin. They're unauthorized.
        if (!requestingUser.getId().equals(uid) && !requestingUser.containsRole(Role.ADMIN)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<User> userOptional = userService.getUserById(uid);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = userOptional.get();

        updateUserFields(requestingUser,user,userDTO);
        return new ResponseEntity<>(new UserGetDTO(userService.updateUser(user)), HttpStatus.OK);
    }

    /**
     * Deletes user from database
     * @param uid ID of user
     * @return ResponseEntity if deletion was successful.
     */
    @DeleteMapping("/api/user/{uid}")
    public @ResponseBody ResponseEntity<Void> deleteUser(@PathVariable long uid) {
        userService.deleteUser(uid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void updateUserFields(User requestingUser, User user, UserDTO userDTO) {
        user.setEmail(Objects.nonNull(userDTO.getEmail()) ? userDTO.getEmail() : user.getEmail());
        // validation todo
        user.setUsername(Objects.nonNull(userDTO.getUsername()) ? userDTO.getUsername() : user.getUsername());
        user.setEnabled(Objects.nonNull(userDTO.getEnabled()) ? userDTO.getEnabled() : user.getEnabled());

        // User must be admin to do the following
        if (requestingUser.containsRole(Role.ADMIN)) {
            user.setLocked(Objects.nonNull(userDTO.getLocked()) ? userDTO.getLocked() : user.getLocked());
            user.setUserRoles(Objects.nonNull(userDTO.getUserRoles()) ? userDTO.getUserRoles() : user.getUserRoles());
        }
        user.getUserDetails().setBio(Objects.nonNull(userDTO.getBio()) ? userDTO.getBio() : user.getUserDetails().getBio());
        if (userDTO.getProfilePicture() != null) {
            MultipartFile pfp = userDTO.getProfilePicture();
            Path path = fileStorageService.save(pfp);
            user.getUserDetails().setProfilePicturePath(path.toString());
        }
    }
}
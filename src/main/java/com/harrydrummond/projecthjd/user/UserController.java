package com.harrydrummond.projecthjd.user;

import com.harrydrummond.projecthjd.user.roles.Role;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@AllArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    /**
     * Gets the user defined in the path
     * @param uid ID Of the user
     * @return ResponseEntity with user details and http status. Returns NOT FOUND if user with id is not found.
     */
    @GetMapping("/api/user/{uid}")
    public ResponseEntity<User> getUser(@PathVariable long uid) {
        Optional<User> user = userService.getUserById(uid);
        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    /**
     * Updates user details
     * @param uid ID of user
     * @param userDTO User data transfer object containing values to update
     * @return ResponseEntity of updated values and http status. Returns NOT FOUND if user with id is not found.
     */
    @PatchMapping("/api/user/{uid}")
    public ResponseEntity<User> updateUser(@AuthenticationPrincipal User requestingUser, @PathVariable long uid, @RequestBody UserDTO userDTO) {

        // If user is not the same user as requesting to update and user is not an admin. They're unauthorized.
        if (!requestingUser.getId().equals(uid) && !requestingUser.containsRole(Role.ADMIN)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<User> userOptional = userService.getUserById(uid);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = userOptional.get();

        user.setEmail(userDTO.getEmail());
        // validation todo
        user.setUsername(userDTO.getUsername());
        user.setEnabled(userDTO.isEnabled());
        // user cannot lock their own account
        user.setLocked(userDTO.isLocked());
        // user cannot set their own roles unless admin
        user.setUserRoles(userDTO.getUserRoles());

        User updatedUser = userService.updateUser(user);

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    /**
     * Deletes user from database
     * @param uid ID of user
     * @return ResponseEntity if deletion was successful.
     */
    @DeleteMapping("/api/user/{uid}")
    public ResponseEntity<Void> deleteUser(@PathVariable long uid) {
        userService.deleteUser(uid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
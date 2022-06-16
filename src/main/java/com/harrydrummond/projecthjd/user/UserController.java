package com.harrydrummond.projecthjd.user;

import com.harrydrummond.projecthjd.filestorage.FileStorageService;
import com.harrydrummond.projecthjd.user.details.UserDetailsRepository;
import com.harrydrummond.projecthjd.user.dto.UserDTO;
import com.harrydrummond.projecthjd.user.dto.UserGetDTO;
import com.harrydrummond.projecthjd.user.dto.UserPreferencesDTO;
import com.harrydrummond.projecthjd.user.preferences.Preference;
import com.harrydrummond.projecthjd.user.preferences.Preferences;
import com.harrydrummond.projecthjd.user.preferences.PreferencesRepository;
import com.harrydrummond.projecthjd.user.roles.Role;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Controller
public class UserController {

    private final UserService userService;
    private final UserDetailsRepository userDetailsRepository;
    private final PreferencesRepository preferencesRepository;
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

    @PostMapping(value = "/api/user/{uid}",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> updateUserForm(@AuthenticationPrincipal @NotNull User requestingUser, @PathVariable long uid, @Valid UserDTO userDTO, BindingResult result) {
        return updateUser(requestingUser, uid, userDTO, result);
    }

        /**
         * Updates user details
         * @param uid ID of user
         * @param userDTO User data transfer object containing values to update
         * @return ResponseEntity of updated values and http status. Returns NOT FOUND if user with id is not found.
         */
    @PostMapping(value = "/api/user/{uid}", consumes = "application/json")
    public @ResponseBody ResponseEntity<Object> updateUser(@AuthenticationPrincipal @NotNull User requestingUser, @PathVariable long uid, @Valid @RequestBody UserDTO userDTO, BindingResult result) {
        // If user is not the same user as requesting to update and user is not an admin. They're unauthorized.
        if (!requestingUser.getId().equals(uid) && !requestingUser.containsRole(Role.ADMIN)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<User> userOptional = userService.getUserById(uid);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = userOptional.get();

        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .filter(x -> !x.getField().equalsIgnoreCase("username") || !requestingUser.getUsername().equals(userDTO.getUsername()))
                    .collect(
                            Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)
                    );

            if (!errors.isEmpty())
                return new ResponseEntity<>(errors, HttpStatus.valueOf(400));
        }

        if (requestingUser.getId().equals(user.getId())) {
            updateUserFields(requestingUser, requestingUser, userDTO);
        } else {
            updateUserFields(requestingUser, user, userDTO);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/api/user/{uid}/preferences", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> updateUserPreferencesForm(@AuthenticationPrincipal @NotNull User requestingUser, @PathVariable long uid, @Valid UserPreferencesDTO userDTO, BindingResult result) {
        if (!requestingUser.getId().equals(uid) && !requestingUser.containsRole(Role.ADMIN)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<User> userOptional = userService.getUserById(uid);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = userOptional.get();

        if (user.getId().equals(requestingUser.getId())) {
            user = requestingUser;
        }

        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(
                            Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)
                    );

            return new ResponseEntity<>(errors, HttpStatus.valueOf(400));
        }

            Preferences preference = preferencesRepository.getByPreference(Preference.ACCOUNT_SUMMARY);
            updatePreference(user, preference, userDTO.isAccountSummary());

            Preferences preference2 = preferencesRepository.getByPreference(Preference.PROMOTIONS);
            updatePreference(user, preference2, userDTO.isPromotions());

            Preferences preference3 = preferencesRepository.getByPreference(Preference.BRAND_INFO);
            updatePreference(user, preference3, userDTO.isBrandInfo());

        User u = userService.updateUser(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void updatePreference(User user, Preferences preferences, boolean bool) {
        if (bool) {
            if (!user.containsPreference(preferences.getPreference()))
                user.getUserPreferences().add(preferences);
        } else {
            List<Preferences> toRemove = new ArrayList<>();
            user.getUserPreferences().stream().filter(x -> x.getPreference().equals(preferences.getPreference())).forEach(toRemove::add);
            toRemove.forEach(x -> user.getUserPreferences().remove(x));
        }

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

        user.setUsername(Objects.nonNull(userDTO.getUsername()) ? userDTO.getUsername() : user.getUsername());
        user.setEnabled(Objects.nonNull(userDTO.getEnabled()) ? userDTO.getEnabled() : user.getEnabled());
        user.getUserDetails().setLink(Objects.nonNull(userDTO.getLink()) ? userDTO.getLink() : user.getUserDetails().getLink());


        // User must be admin to do the following
        if (requestingUser.containsRole(Role.ADMIN)) {
            user.setLocked(Objects.nonNull(userDTO.getLocked()) ? userDTO.getLocked() : user.getLocked());
            user.setUserRoles(Objects.nonNull(userDTO.getUserRoles()) ? userDTO.getUserRoles() : user.getUserRoles());
        }
        user.getUserDetails().setBio(Objects.nonNull(userDTO.getBio()) ? userDTO.getBio() : user.getUserDetails().getBio());
        if (userDTO.getProfilePicture() != null && !userDTO.getProfilePicture().isEmpty() && userDTO.getProfilePicture().getSize() > 0) {
            MultipartFile pfp = userDTO.getProfilePicture();
            Path path = fileStorageService.save(pfp);
            user.getUserDetails().setProfilePicturePath(path.toString());
        }

        User u = userService.updateUser(user);
        userDetailsRepository.save(user.getUserDetails());
    }
}
package com.harrydrummond.projecthjd.user;

import com.harrydrummond.projecthjd.user.details.UserDetails;
import com.harrydrummond.projecthjd.user.details.UserDetailsRepository;
import com.harrydrummond.projecthjd.user.roles.Role;
import com.harrydrummond.projecthjd.user.roles.RoleRepository;
import com.harrydrummond.projecthjd.user.roles.UserRole;
import lombok.AllArgsConstructor;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;
import java.util.*;


@Service
@AllArgsConstructor
public class UserServiceImpl extends DefaultOAuth2UserService implements UserService {

    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final RoleRepository roleRepository;

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> getUserById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }



    @Override
    public User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User user =  super.loadUser(oAuth2UserRequest);
        String email = user.getAttributes().get("email").toString();
        Optional<User> appUserOptional = userRepository.findByEmail(email);

        if (appUserOptional.isEmpty()) {
            UserRole userRole = roleRepository.getByRole(Role.USER);

            User appUser = new User();
            appUser.setEmail(email);
            appUser.setUserRoles(Set.of(userRole));
            appUser.setProvider(Provider.GOOGLE);
            appUser.setAttributes(user.getAttributes()); //to the user at the top of this method
            appUser.setUsername(generateUsername(email));
            appUser.setDateCreated(LocalDateTime.now());

            UserDetails userDetails = new UserDetails();
            userDetails.setUser(appUser);

            appUser.setUserDetails(userDetails);

            return userRepository.save(appUser);
        } else if (!appUserOptional.get().getEnabled()) {
            // if account is disabled, enable it
            appUserOptional.get().setEnabled(true);
        }
        return appUserOptional.get();
    }

    private String generateUsername(String email) {
        String username = email;
        if (username.contains("@")) {
            username = username.substring(0, username.indexOf("@"));
        }
        username = username.replaceAll("[^a-zA-Z0-9]", "");
        username = username.substring(0, Math.min(username.length(), 20));
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            username = "user-" + UUID.randomUUID().toString().substring(0, 5);
        }

        return username;
    }
}
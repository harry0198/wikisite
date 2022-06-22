package com.harrydrummond.projecthjd.user;

import com.harrydrummond.projecthjd.filestorage.FileStorageService;
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

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;


@Service
@AllArgsConstructor
public class UserServiceImpl extends DefaultOAuth2UserService implements UserService {

    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final RoleRepository roleRepository;

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> getUserByProviderAndOAuthId(Provider provider, String OAuthId) {
        return userRepository.findByProviderAndAuthId(provider, OAuthId);
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

        String providerName = oAuth2UserRequest.getClientRegistration().getClientName();

        System.out.println(user.getAttributes());

        switch(providerName) {
            case "Google":
                return getGoogleUser(user).orElseGet(() -> makeGoogleUser(user));
            case "Facebook":
                return getFacebookUser(user).orElseGet(() -> makeFacebookUser(user));
            case "GitHub":
                return getGithubUser(user).orElseGet(() -> makeGithubUser(user));
            default:
                // error!
        }


        return null;
    }

    public User makeGoogleUser(OAuth2User user) {
        User appUser = makeUser(user);

        String email = user.getAttribute("email").toString();
        String pfp = user.getAttribute("picture");

        if (pfp != null && !pfp.isEmpty()) {
            Path path = fileStorageService.save(pfp);
            appUser.getUserDetails().setProfilePicturePath(path.toString());
        }

        appUser.setUsername(generateUsername(email));
        appUser.setEmail(email);
        appUser.setProvider(Provider.GOOGLE);
        appUser.setAuthId(user.getAttribute("sub"));

        return userRepository.save(appUser);
    }

    public User makeGithubUser(OAuth2User user) {
        User appUser = makeUser(user);

        String name = user.getAttribute("login");
        String pfp = user.getAttribute("avatar_url");
        Object id = user.getAttribute("id");

        if (pfp != null && !pfp.isEmpty()) {
            Path path = fileStorageService.save(pfp);
            appUser.getUserDetails().setProfilePicturePath(path.toString());
        }

        appUser.setUsername(generateUsername(name));
        appUser.setProvider(Provider.GITHUB);
        appUser.setAuthId(id.toString());

        return userRepository.save(appUser);
    }

    public User makeFacebookUser(OAuth2User user) {
        User appUser = makeUser(user);

        String id = user.getAttribute("id").toString();
        String username = user.getAttribute("name").toString();
        String email = user.getAttribute("email").toString();

        appUser.setUsername(generateUsername(username));
        appUser.setEmail(email);
        appUser.setProvider(Provider.FACEBOOK);
        appUser.setAuthId(id);

        return userRepository.save(appUser);
    }

    private User makeUser(OAuth2User user) {
        UserRole userRole = roleRepository.getByRole(Role.USER);

        User appUser = new User();
        appUser.setUserRoles(Set.of(userRole));
        appUser.setAttributes(user.getAttributes());
        appUser.setDateCreated(LocalDateTime.now());

        UserDetails userDetails = new UserDetails();
        userDetails.setUser(appUser);

        appUser.setUserDetails(userDetails);

        return appUser;
    }

    private Optional<User> getGoogleUser(OAuth2User user) {
        Object id = user.getAttribute("sub");
        return userRepository.findByProviderAndAuthId(Provider.GOOGLE, id.toString());
    }

    private Optional<User> getGithubUser(OAuth2User user) {
        Object id = user.getAttribute("id");
        return userRepository.findByProviderAndAuthId(Provider.GITHUB, id.toString());
    }

    private Optional<User> getFacebookUser(OAuth2User user) {
        Object id = user.getAttribute("id");
        return userRepository.findByProviderAndAuthId(Provider.FACEBOOK, id.toString());
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
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
import org.springframework.validation.Validator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
@AllArgsConstructor
public class UserServiceImpl extends DefaultOAuth2UserService implements UserService {

    private Validator validator;
    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final RoleRepository roleRepository;

    public Optional<User> findByUsername(String username) {
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
            appUser.setUsername(email);
            appUser.setDateCreated(LocalDateTime.now());

            UserDetails userDetails = new UserDetails();
            userDetails.setUser(appUser);

            appUser.setUserDetails(userDetails);

            userDetailsRepository.save(userDetails);
            return userRepository.save(appUser);
        }
        return appUserOptional.get();
    }
}
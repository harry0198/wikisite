package com.harrydrummond.wikisite.appuser;

import lombok.AllArgsConstructor;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class UserServiceImpl extends DefaultOAuth2UserService implements UserService {

    private final UserRepository userRepository;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> getUserById(long id) {
        return userRepository.findById(id);
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
            User appUser = new User();
            appUser.setEmail(email);
            appUser.setUserRole(UserRole.USER);
            appUser.setProvider(Provider.GOOGLE);
            appUser.setAttributes(user.getAttributes()); //to the user at the top of this method
            appUser.setUsername(email);

            userRepository.save(appUser);
            return appUser;
        }
        return appUserOptional.get();
    }
}
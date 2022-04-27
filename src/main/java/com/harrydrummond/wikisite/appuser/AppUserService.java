package com.harrydrummond.wikisite.appuser;

import lombok.AllArgsConstructor;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@AllArgsConstructor
public class AppUserService extends DefaultOAuth2UserService {

    private final AppUserRepository appUserRepository;

    public AppUser getAppUserByEmail(String email) {
        return appUserRepository.findByEmail(email).orElseThrow(() -> new IllegalStateException("user does not exist"));
    }

    public void signUpUser(AppUser appUser) {

        boolean userExists = appUserRepository.findByEmail(appUser.getEmail()).isPresent();

        if (userExists) {
            throw new IllegalStateException("email already taken");
        }

        appUserRepository.save(appUser);
    }

    @Override
    public AppUser loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User user =  super.loadUser(oAuth2UserRequest);
        String email = user.getAttributes().get("email").toString();
        Optional<AppUser> appUserOptional = appUserRepository.findByEmail(email);

        if (appUserOptional.isEmpty()) {
            AppUser appUser = new AppUser();
            appUser.setEmail(email);
            appUser.setAppUserRole(AppUserRole.USER);
            appUser.setProvider(Provider.GOOGLE);
            appUser.setAttributes(user.getAttributes()); //to the useer at the top of this method
            appUser.setName(email);

            return appUser;
        }
        return appUserOptional.get();
    }

}
package com.harrydrummond.wikisite.appuser;

import com.harrydrummond.wikisite.appuser.likes.AppUserLikes;
import com.harrydrummond.wikisite.appuser.likes.AppUserLikesRepository;
import com.harrydrummond.wikisite.appuser.saves.AppUserSaves;
import com.harrydrummond.wikisite.appuser.saves.AppUserSavesRepository;
import com.harrydrummond.wikisite.posts.Post;
import com.harrydrummond.wikisite.posts.PostService;
import lombok.AllArgsConstructor;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@AllArgsConstructor
public class AppUserService extends DefaultOAuth2UserService {

    private final AppUserRepository appUserRepository;
    private final AppUserSavesRepository appUserSavesRepository;
    private final AppUserLikesRepository appUserLikesRepository;

    private final PostService postService;

    public void savePost(AppUser appUser, int postId, boolean save) {
        Post post = postService.getPostById(postId);

        AppUserSaves appUserSaves =  appUserSavesRepository.findByPostAndAppUser(post, appUser);
        if (save) {
            if (appUserSaves == null) {
                appUserSaves = new AppUserSaves(post, appUser);
                appUser.addSave(appUserSaves);
                appUserRepository.save(appUser);
            }

        } else {
            appUser.removeSave(appUserSaves);
            appUserRepository.save(appUser);
        }
    }

    public void likePost(AppUser appUser, int postId, boolean like) {
        Post post = postService.getPostById(postId);

        AppUserLikes appUserLikes = appUserLikesRepository.findByPostAndAppUser(post, appUser);
        if (like) {
            if (appUserLikes == null) {
                appUserLikes = new AppUserLikes(post, appUser);
                appUser.addLike(appUserLikes);
                appUserRepository.save(appUser);
            }

        } else {
            appUser.removeLike(appUserLikes);
            appUserRepository.save(appUser);
        }
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
            appUser.setAttributes(user.getAttributes()); //to the user at the top of this method
            appUser.setName(email);

            appUserRepository.save(appUser);
            return appUser;
        }
        return appUserOptional.get();
    }

}
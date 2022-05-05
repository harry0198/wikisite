package com.harrydrummond.wikisite.appuser;

import com.harrydrummond.wikisite.appuser.likes.UserLikes;
import com.harrydrummond.wikisite.appuser.likes.UserLikesRepository;
import com.harrydrummond.wikisite.appuser.saves.UserSaves;
import com.harrydrummond.wikisite.appuser.saves.UserSavesRepository;
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
public class UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final UserSavesRepository userSavesRepository;
    private final UserLikesRepository userLikesRepository;

    private final PostService postService;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void savePost(User user, int postId, boolean save) {
        Post post = postService.getPostById(postId);

        UserSaves userSaves =  userSavesRepository.findByPostAndUser(post, user);
        if (save) {
            if (userSaves == null) {
                userSaves = new UserSaves(post, user);
                user.addSave(userSaves);
                userRepository.save(user);
            }

        } else {
            user.removeSave(userSaves);
            userRepository.save(user);
        }
    }

    public void likePost(User user, int postId, boolean like) {
        Post post = postService.getPostById(postId);

        UserLikes userLikes = userLikesRepository.findByPostAndUser(post, user);
        if (like) {
            if (userLikes == null) {
                userLikes = new UserLikes(post, user);
                user.addLike(userLikes);
                userRepository.save(user);
            }

        } else {
            user.removeLike(userLikes);
            userRepository.save(user);
        }
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
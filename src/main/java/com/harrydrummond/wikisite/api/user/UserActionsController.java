package com.harrydrummond.wikisite.api.user;

import com.harrydrummond.wikisite.api.post.PostLikeRequest;
import com.harrydrummond.wikisite.api.post.PostSaveRequest;
import com.harrydrummond.wikisite.appuser.User;
import com.harrydrummond.wikisite.appuser.UserRole;
import com.harrydrummond.wikisite.appuser.UserServiceImpl;
import com.harrydrummond.wikisite.util.Validate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UserActionsController extends AbstractUserController{


    public UserActionsController(UserServiceImpl userService) {
        super(userService);
    }

    @PostMapping("/api/v1/post/save")
    public void saveArticleHandler(@AuthenticationPrincipal User user, @RequestBody PostSaveRequest postSaveRequest) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

//        getUserService().savePost(user, postSaveRequest.getPostId(), postSaveRequest.isSave());
    }

    @PostMapping("/api/v1/post/like")
    public void likeArticleHandler(@AuthenticationPrincipal User user, @RequestBody PostLikeRequest postLikeRequest) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

//        getUserService().likePost(user, postLikeRequest.getPostId(), postLikeRequest.isLike());
    }

    /**
     * Updates the username of the user with the id provided. If the user is not authorized to edit the
     * provided username a UNAUTHORIZED http code is thrown. If max length exceeds 16chars a BAD_REQUEST http code
     * is thrown. If username exists a CONFLICT http code is thrown.
     * @param authenticatedUser Authenticated user
     * @param username Username to change user to.
     */
    @PostMapping("/api/v1/users/{id}/username")
    public void updateUsername(@PathVariable int id, @AuthenticationPrincipal User authenticatedUser, String username) {
        User user = validateAndGetUserFromId(id);

        if (authenticatedUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        if (!user.getId().equals(authenticatedUser.getId())) {
            if ( !authenticatedUser.getUserRole().equals(UserRole.ADMIN)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
        }

        boolean validated = Validate.validateInputLength(username, 16);
        if (!validated) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username was of invalid length (max 16 characters)");
        }

        if (getUserService().findByUsername(username).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }

        user.setUsername(username);
        getUserService().saveUser(user);
    }

    /**
     * Gets username of user.
     * @param id Id of user to retrieve
     * @return String username of user or
     */
    @GetMapping(value = "/api/v1/users/{id}/username", produces = "text/plain")
    public String getUsername(@PathVariable int id) {
        User user = validateAndGetUserFromId(id);
        return user.getUsername();
    }
}
package com.harrydrummond.wikisite.api.user;

import com.harrydrummond.wikisite.api.post.PostLikeRequest;
import com.harrydrummond.wikisite.api.post.PostSaveRequest;
import com.harrydrummond.wikisite.appuser.AppUser;
import com.harrydrummond.wikisite.appuser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
@RestController
public class UserActionsController {

    private final AppUserService appUserService;

    @PostMapping("/api/v1/post/save")
    public void saveArticleHandler(@AuthenticationPrincipal AppUser appUser, @RequestBody PostSaveRequest postSaveRequest) {
        if (appUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        appUserService.savePost(appUser, postSaveRequest.getPostId(), postSaveRequest.isSave());
    }

    @PostMapping("/api/v1/post/like")
    public void likeArticleHandler(@AuthenticationPrincipal AppUser appUser, @RequestBody PostLikeRequest postLikeRequest) {
        if (appUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        appUserService.likePost(appUser, postLikeRequest.getPostId(), postLikeRequest.isLike());
    }
}
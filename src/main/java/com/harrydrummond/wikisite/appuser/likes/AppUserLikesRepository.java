package com.harrydrummond.wikisite.appuser.likes;

import com.harrydrummond.wikisite.appuser.AppUser;
import com.harrydrummond.wikisite.posts.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserLikesRepository extends JpaRepository<AppUserLikes, Long> {

    AppUserLikes findByPostAndAppUser(Post post, AppUser appUser);
}
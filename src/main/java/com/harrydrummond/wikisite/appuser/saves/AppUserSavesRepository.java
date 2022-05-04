package com.harrydrummond.wikisite.appuser.saves;

import com.harrydrummond.wikisite.appuser.AppUser;
import com.harrydrummond.wikisite.posts.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserSavesRepository extends JpaRepository<AppUserSaves, Long> {

    AppUserSaves findByPostAndAppUser(Post post, AppUser appUser);
}
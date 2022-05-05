package com.harrydrummond.wikisite.appuser.saves;

import com.harrydrummond.wikisite.appuser.User;
import com.harrydrummond.wikisite.posts.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSavesRepository extends JpaRepository<UserSaves, Long> {

    UserSaves findByPostAndUser(Post post, User user);
}
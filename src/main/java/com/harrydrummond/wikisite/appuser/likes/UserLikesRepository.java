package com.harrydrummond.wikisite.appuser.likes;

import com.harrydrummond.wikisite.appuser.User;
import com.harrydrummond.wikisite.posts.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLikesRepository extends JpaRepository<UserLikes, Long> {

    UserLikes findByPostAndUser(Post post, User user);
}
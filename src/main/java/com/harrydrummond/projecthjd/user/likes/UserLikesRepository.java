package com.harrydrummond.projecthjd.user.likes;

import com.harrydrummond.projecthjd.user.User;
import com.harrydrummond.projecthjd.posts.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLikesRepository extends JpaRepository<UserLikes, Long> {

    UserLikes findByPostAndUser(Post post, User user);
}
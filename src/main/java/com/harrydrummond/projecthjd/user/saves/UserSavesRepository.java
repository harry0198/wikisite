package com.harrydrummond.projecthjd.user.saves;

import com.harrydrummond.projecthjd.user.User;
import com.harrydrummond.projecthjd.posts.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSavesRepository extends JpaRepository<UserSaves, Long> {

    UserSaves findByPostAndUser(Post post, User user);
}
package com.harrydrummond.projecthjd.user;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> getUserById(long id);

    Optional<User> getUserByEmail(String email);
    Optional<User> getUserByUsername(String username);

    Optional<User> getUserByProviderAndOAuthId(Provider provider, String OAuthId);

    User saveUser(User user);

    List<User> getAllUsers();

    User updateUser(User user);

    void deleteUser(long id);

}
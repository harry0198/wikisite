package com.harrydrummond.projecthjd.user;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User loadPreferences(User user);

    Optional<User> getUserById(long id);

    Optional<User> getUserByEmail(String email);
    Optional<User> getUserByUsername(String username);

    User saveUser(User user);

    List<User> getAllUsers();

    User updateUser(User user);

    void deleteUser(long id);

}
package com.harrydrummond.projecthjd.user;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> getUserById(long id);

    User saveUser(User user);

    List<User> getAllUsers();

    User updateUser(User user);

    void deleteUser(long id);

}
package com.harrydrummond.wikisite.api.user;

import com.harrydrummond.wikisite.appuser.User;
import com.harrydrummond.wikisite.appuser.UserServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Getter
@Service
@AllArgsConstructor
public abstract class AbstractUserController {

    private final UserServiceImpl userService;

    protected User validateAndGetUserFromId(int id) {
        Optional<User> user = userService.getUserById(id);

        if (user.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id does not exist");

        return user.get();
    }
}
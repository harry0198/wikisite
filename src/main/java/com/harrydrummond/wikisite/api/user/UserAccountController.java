package com.harrydrummond.wikisite.api.user;

import com.harrydrummond.wikisite.appuser.UserService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserAccountController extends AbstractUserController {

    public UserAccountController(UserService userService) {
        super(userService);
    }
}
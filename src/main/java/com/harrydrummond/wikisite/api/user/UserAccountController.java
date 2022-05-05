package com.harrydrummond.wikisite.api.user;

import com.harrydrummond.wikisite.appuser.UserServiceImpl;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserAccountController extends AbstractUserController {

    public UserAccountController(UserServiceImpl userService) {
        super(userService);
    }
}
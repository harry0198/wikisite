package com.harrydrummond.projecthjd.api.user;

import com.harrydrummond.projecthjd.user.UserServiceImpl;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserAccountController extends AbstractUserController {

    public UserAccountController(UserServiceImpl userService) {
        super(userService);
    }
}
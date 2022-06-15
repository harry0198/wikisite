package com.harrydrummond.projecthjd.validators;

import com.harrydrummond.projecthjd.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements
        ConstraintValidator<UniqueUsernameConstraint, String> {

    @Autowired private UserService userService;

    @Override
    public void initialize(UniqueUsernameConstraint uniqueUsernameConstraint) {
    }

    @Override
    public boolean isValid(String usernameField,
                           ConstraintValidatorContext cxt) {
        return userService.getUserByUsername(usernameField).isEmpty();
    }

}
package com.harrydrummond.projecthjd.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class LinkValidator implements
        ConstraintValidator<LinkConstraint, String> {

    @Override
    public void initialize(LinkConstraint linkConstraint) {
    }

    @Override
    public boolean isValid(String link,
                           ConstraintValidatorContext cxt) {

        if (link.length() <= 0) return true;
        Pattern pNoEmbed = Pattern.compile("[(http(s)?):\\/\\/(www\\.)?a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)");

        return pNoEmbed.matcher(link).matches();
    }

}
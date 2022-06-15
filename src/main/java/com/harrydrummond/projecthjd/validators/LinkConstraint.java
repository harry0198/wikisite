package com.harrydrummond.projecthjd.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LinkValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface LinkConstraint {
    String message() default "Must be a link!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
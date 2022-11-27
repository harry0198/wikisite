package com.harrydrummond.projecthjd.validators.image;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ImageValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ImageConstraint {
    String message() default "Could not validate provided file type as image";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
package com.harrydrummond.projecthjd.validators.image;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SingleImageValidator extends FileValidator implements ConstraintValidator<SingleImageConstraint, MultipartFile> {

    @Override
    public void initialize(SingleImageConstraint imageConstraint) {
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        if (file == null) return true;
        return validateFile(file);
    }
}
package com.harrydrummond.projecthjd.validators.image;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImageValidator extends FileValidator implements ConstraintValidator<ImageConstraint, MultipartFile[]> {

    @Override
    public void initialize(ImageConstraint imageConstraint) {
    }

    @Override
    public boolean isValid(MultipartFile[] files, ConstraintValidatorContext constraintValidatorContext) {
        if (files == null) return true;
        for (MultipartFile file : files) {
            boolean result = validateFile(file);
            if (!result) {
                return false;
            }
        }
        return true;
    }

}
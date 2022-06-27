package com.harrydrummond.projecthjd.validators;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.io.InputStream;

public class ImageValidator implements ConstraintValidator<ImageConstraint, MultipartFile> {

    @Override
    public void initialize(ImageConstraint imageConstraint) {
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        if (file == null || file.isEmpty()) return true;

        String fileType = file.getContentType();
        if (fileType == null) return false;
        if (fileType.equals("image/png") || fileType.equals("image/jpeg") || fileType.equals("image/jpg") || fileType.equals("image/gif") ||
        fileType.equals("image/heic") || fileType.equals("image/heic-sequence")) {

            try {
                InputStream input = file.getInputStream();
                ImageIO.read(input).toString();
                return true;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

}
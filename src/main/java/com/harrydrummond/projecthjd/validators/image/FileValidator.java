package com.harrydrummond.projecthjd.validators.image;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;

public class FileValidator {
    public boolean validateFile(MultipartFile file) {
        if (file == null) return true;
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
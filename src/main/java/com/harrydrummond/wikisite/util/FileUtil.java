package com.harrydrummond.wikisite.util;

import com.harrydrummond.wikisite.appuser.AppUser;
import com.harrydrummond.wikisite.util.exceptions.CopyFileException;
import com.harrydrummond.wikisite.util.exceptions.FileTooLargeException;
import com.harrydrummond.wikisite.util.exceptions.InvalidFileTypeException;
import com.harrydrummond.wikisite.util.exceptions.MissingFileException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileUtil {

    private static final List<String> imageFileTypes = List.of("jpg", "png");

    public static void saveFile(MultipartFile file, AppUser user, Path rootLocation) throws CopyFileException, MissingFileException {
        String newFileName = getNewFileName(user, file);

        try (InputStream is = file.getInputStream()) {
            Files.copy(is, rootLocation.resolve(newFileName));
        } catch (IOException ie) {

            throw new CopyFileException("Failed to upload!");
        }
    }

    private static String getNewFileName(AppUser appUser, MultipartFile file) {
        return String.format("%s-%s--%s", appUser.getId(), System.currentTimeMillis(), file.getName());
    }


    public static void validateImage(MultipartFile file) throws InvalidFileTypeException, MissingFileException {
        checkFileExistence(file);
        if (file.getOriginalFilename() == null || !imageFileTypes.contains(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")))) {
            throw new InvalidFileTypeException("Invalid image file type extension");
        }
    }

    private static void checkFileExistence(MultipartFile file) throws MissingFileException {
        if (file == null)
            throw new MissingFileException("No file sent!");
        if (file.getName().equals(""))
            throw new MissingFileException("No file sent!");
    }
}
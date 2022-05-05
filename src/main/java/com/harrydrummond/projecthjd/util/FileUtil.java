package com.harrydrummond.projecthjd.util;

import com.harrydrummond.projecthjd.user.User;
import com.harrydrummond.projecthjd.util.exceptions.CopyFileException;
import com.harrydrummond.projecthjd.util.exceptions.InvalidFileTypeException;
import com.harrydrummond.projecthjd.util.exceptions.MissingFileException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileUtil {

    private static final List<String> imageFileTypes = List.of("jpg", "png");

    public static void saveFile(MultipartFile file, User user, Path rootLocation) throws CopyFileException, MissingFileException {
        String newFileName = getNewFileName(user, file);

        try (InputStream is = file.getInputStream()) {
            Files.copy(is, rootLocation.resolve(newFileName));
        } catch (IOException ie) {

            throw new CopyFileException("Failed to upload!");
        }
    }

    private static String getNewFileName(User user, MultipartFile file) {
        return String.format("%s-%s--%s", user.getId(), System.currentTimeMillis(), file.getName());
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
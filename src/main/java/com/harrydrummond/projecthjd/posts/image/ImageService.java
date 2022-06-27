package com.harrydrummond.projecthjd.posts.image;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public interface ImageService {

    Optional<Image> getImageById(long id);

    Image saveImage(Image image, File file);
    Image saveImage(Image image, MultipartFile file);
    Image saveImage(Image image, String link);

    List<Image> getAllImages();

    Image updateImage(Image image);

    void deleteImage(long id);
}
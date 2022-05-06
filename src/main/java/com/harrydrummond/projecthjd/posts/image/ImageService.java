package com.harrydrummond.projecthjd.posts.image;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ImageService {

    Optional<Image> getImageById(long id);

    Image saveImage(Image image);

    File saveImageToFileOnly(Image image, MultipartFile file) throws IOException;
    Image saveImageWithFile(Image image, MultipartFile file) throws IOException;

    List<Image> getAllImages();

    Image updateImage(Image image);

    void deleteImage(long id);
}
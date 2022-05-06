package com.harrydrummond.projecthjd.posts.image;

import java.util.List;
import java.util.Optional;

public interface ImageService {

    Optional<Image> getImageById(long id);

    Image saveImage(Image image);

    List<Image> getAllImages();

    Image updateImage(Image image);

    void deleteImage(long id);
}
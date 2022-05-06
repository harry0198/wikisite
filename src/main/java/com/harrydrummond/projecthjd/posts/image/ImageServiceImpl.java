package com.harrydrummond.projecthjd.posts.image;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Override
    public Optional<Image> getImageById(long id) {
        return imageRepository.findById(id);
    }

    @Override
    public Image saveImage(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public Image saveImageWithFile(Image image, MultipartFile file) throws IOException {
        saveImageToFileOnly(image, file);

        return imageRepository.save(image);
    }

    @Override
    public File saveImageToFileOnly(Image image, MultipartFile file) throws IOException {
        File savedFile = new File(getFilePathForImage(image));
        file.transferTo(savedFile);
        return savedFile;
    }

    @Override
    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    @Override
    public Image updateImage(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public void deleteImage(long id) {
        imageRepository.deleteById(id);
    }

    // Generates file path for given image. Formatted using title, UUID and alt text
    private String getFilePathForImage(Image image) {
        String alt = image.getAlt(); // is not required

        return String.format("%s-%s:%s",
                image.getPost().getTitle().toLowerCase().substring(0,12),
                UUID.randomUUID().toString().substring(0,5),
                alt
                );

    }
}
package com.harrydrummond.projecthjd.posts.image;

import com.harrydrummond.projecthjd.filestorage.FileStorageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final FileStorageService fileStorageService;

    @Override
    public Optional<Image> getImageById(long id) {
        return imageRepository.findById(id);
    }

    @Override
    public Image saveImage(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public Image saveImageWithFile(Image image, MultipartFile file) {
        saveImageToFileOnly(image, file);

        return imageRepository.save(image);
    }

    @Override
    public Path saveImageToFileOnly(Image image, MultipartFile file) {
        return fileStorageService.save(file);
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
}
package com.harrydrummond.projecthjd.posts.image;

import com.harrydrummond.projecthjd.filestorage.FileStorageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
    public Image saveImage(Image image, File file) {
        return handleSaveImage(image, fileStorageService.save(file));
    }

    @Override
    public Image saveImage(Image image, MultipartFile file) {
        return handleSaveImage(image, fileStorageService.save(file));
    }

    @Override
    public Image saveImage(Image image, String link) {
        return handleSaveImage(image, fileStorageService.save(link));
    }

    private Image handleSaveImage(Image image, CompletableFuture<Path> cf) {
        Path path;
        try {
            path = cf.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        image.setPath(path.toString());
        return imageRepository.save(image);
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
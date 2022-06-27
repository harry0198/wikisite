package com.harrydrummond.projecthjd.filestorage;

import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    CompletableFuture<Path> save(MultipartFile file);

    CompletableFuture<Path> save(String url);

    CompletableFuture<Path> save(File file);

    CompletableFuture<S3ObjectInputStream> findByName(String fileName);
}
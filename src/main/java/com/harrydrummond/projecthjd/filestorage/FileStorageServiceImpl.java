package com.harrydrummond.projecthjd.filestorage;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path root = Paths.get("uploads", "post", "images");
    @Override
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }
    @Override
    public Path save(MultipartFile file) {
        try {
            String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            Files.createDirectories(root.resolve(format));
            Path path = this.root.resolve(format + File.separator + UUID.randomUUID().toString().substring(0,5) + "-" + file.getOriginalFilename().toLowerCase().substring(0, Math.min(file
                    .getOriginalFilename().length(), 6)));
            Files.copy(file.getInputStream(), path);
            return path;
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public Path save(String link) {
        try {
            URL url = new URL(link);
            InputStream is = url.openStream();


            Files.createDirectories(root.resolve("THIRD_PARTY_DOWNLOADS"));
            Path path = this.root.resolve( "THIRD_PARTY_DOWNLOADS" + File.separator + "LINK_DOWNLOAD-" + UUID.randomUUID().toString().substring(0,5));
            Files.copy(is, path);
            return path;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }
    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }
    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
}
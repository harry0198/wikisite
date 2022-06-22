package com.harrydrummond.projecthjd.filestorage;

import java.awt.*;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    void init();
    Path save(MultipartFile file);

    Path save(String url);
    Resource load(String filename);
    void deleteAll();
    Stream<Path> loadAll();
}
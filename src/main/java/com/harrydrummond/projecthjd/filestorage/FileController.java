package com.harrydrummond.projecthjd.filestorage;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@AllArgsConstructor
@RestController
public class FileController {
    private FileStorageService fileStorageService;

    private static final Logger LOG = LoggerFactory.getLogger(FileController.class);

    @GetMapping("/uploads/**")
    public ResponseEntity<Object> findByName(HttpServletRequest request) {

        String[] params = request.getRequestURI().split(request.getContextPath() + "/uploads/");
        if (params.length < 2) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        String fileName = params[1];

        S3ObjectInputStream file;

        try {
            file = fileStorageService.findByName(fileName).handle((rs, ex) -> {
                if (ex != null) {
                    LOG.warn("Failed file fetch with error {} on file {}", ex.getMessage(), fileName);
                    return null;
                }
                return rs;
            }).get(10, TimeUnit.SECONDS);

        } catch (InterruptedException | TimeoutException e) {
            return new ResponseEntity<>(HttpStatus.REQUEST_TIMEOUT);
        } catch (ExecutionException e) {
            LOG.error("Error while fetching file", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (file == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.noCache())
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body(new InputStreamResource(file));

    }
}
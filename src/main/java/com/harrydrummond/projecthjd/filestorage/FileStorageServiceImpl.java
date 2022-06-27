package com.harrydrummond.projecthjd.filestorage;


import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
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
import java.util.concurrent.CompletableFuture;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${s3.bucket.name}")
    private String s3BucketName;

    private static final Logger LOG = LoggerFactory.getLogger(FileStorageServiceImpl.class);

    private File convertMultiPartFileToFile(final MultipartFile multipartFile) {
        final File file = new File(multipartFile.getOriginalFilename());
        try (final FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        } catch (IOException e) {
            LOG.error("Error {} occurred while converting the multipart file", e.getLocalizedMessage());
        }
        return file;
    }

    private File convertInputStreamToFile(final InputStream inputStream) {
        File file = new File(UUID.randomUUID().toString());

        try (final FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(inputStream.readAllBytes());
        } catch (IOException e) {
            LOG.error("Error {} occurred while converting the input stream to file", e.getLocalizedMessage());
        }
        return file;
    }

    @Async
    @Override
    public CompletableFuture<Path> save(MultipartFile multipartFile) {
        return save(convertMultiPartFileToFile(multipartFile));
    }



    @Async
    @Override
    public CompletableFuture<Path> save(String link) {
        try {
            URL url = new URL(link);
            InputStream is = url.openStream();

            return save(convertInputStreamToFile(is));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public CompletableFuture<Path> save(File file) {

        String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        final String fileName = format + File.separator + UUID.randomUUID().toString().substring(0,5) + "-" + file.getName().toLowerCase().substring(0, Math.min(file
                .getName().length(), 6));

        System.out.println("saving...");
        System.out.println(fileName);

        return CompletableFuture.supplyAsync(() -> {

            try {
                LOG.info("Uploading file with name {}", fileName);
                final PutObjectRequest putObjectRequest = new PutObjectRequest(s3BucketName, fileName, file);
                amazonS3.putObject(putObjectRequest);
                Files.delete(file.toPath()); // Remove the file locally created in the project folder
            } catch (AmazonServiceException e) {
                LOG.error("Error {} occurred while uploading file", e.getLocalizedMessage());
            } catch (IOException ex) {
                LOG.error("Error {} occurred while deleting temporary file", ex.getLocalizedMessage());
            }

            return Paths.get(fileName);
        });
    }


    @Override
    @Async
    public CompletableFuture<S3ObjectInputStream> findByName(String fileName) {
        LOG.info("Downloading file with name {}", fileName);
        return CompletableFuture.supplyAsync(() -> amazonS3.getObject(s3BucketName, fileName).getObjectContent());
    }
}
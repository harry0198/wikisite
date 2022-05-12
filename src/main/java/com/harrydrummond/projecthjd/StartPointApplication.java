package com.harrydrummond.projecthjd;

import com.harrydrummond.projecthjd.filestorage.FileStorageService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@AllArgsConstructor
@SpringBootApplication
public class StartPointApplication implements CommandLineRunner {

    @Resource
    private final FileStorageService storageService;

    public static void main(String[] args) {
        SpringApplication.run(StartPointApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        storageService.init();
    }
}
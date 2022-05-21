package com.harrydrummond.projecthjd;

import com.harrydrummond.projecthjd.filestorage.FileStorageService;
import com.harrydrummond.projecthjd.user.roles.Role;
import com.harrydrummond.projecthjd.user.roles.RoleRepository;
import com.harrydrummond.projecthjd.user.roles.UserRole;
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

    private final RoleRepository roleRepository;

    public static void main(String[] args) {
        SpringApplication.run(StartPointApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        storageService.init();

        UserRole userRole = new UserRole();
        userRole.setRole(Role.USER);

        UserRole userAdminRole = new UserRole();
        userAdminRole.setRole(Role.ADMIN);

        roleRepository.save(userRole);
        roleRepository.save(userAdminRole);

    }
}
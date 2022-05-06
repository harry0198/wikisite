package com.harrydrummond.projecthjd.repository;

import com.harrydrummond.projecthjd.user.Provider;
import com.harrydrummond.projecthjd.user.User;
import com.harrydrummond.projecthjd.user.UserRepository;
import com.harrydrummond.projecthjd.user.roles.Role;
import com.harrydrummond.projecthjd.user.roles.UserRole;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional
@TestPropertySource(properties={"spring.jpa.hibernate.ddl-auto=create"})
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private static User user;

    @BeforeAll
    public static void generateUser() {
        user = new User();
        user.setEmail("example@email.com");
        user.setUsername("harry");
        user.setUserRoles(Set.of(new UserRole(Role.USER)));
        user.setProvider(Provider.LOCAL);
        user.setDateCreated(LocalDateTime.now());
        user.setId(1L);
    }


    @Test
    public void checkInit() {
        assertNotNull(userRepository);
    }

    @Test
    void findByEmail() {
        userRepository.save(user);

        Optional<User> foundUserOptional = userRepository.findByEmail("example@email.com");
        assertTrue(foundUserOptional.isPresent());
        assertEquals(foundUserOptional.get().getEmail(), user.getEmail());
    }

    @Test
    void findByUsername() {
        userRepository.save(user);

        Optional<User> foundUserOptional = userRepository.findByUsername("harry");
        assertTrue(foundUserOptional.isPresent());
        assertEquals(foundUserOptional.get().getUsername(), user.getUsername());
    }
}
package com.harrydrummond.wikisite.appuser;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@SpringBootTest
class UserServiceTest {


    @Autowired
    private UserService userService;

    @Test
    public void checkInit() {
        assertNotNull(userService);
    }

    @Test
    public void greetingShouldReturnMessageFromService() throws Exception {

    }
//
//    @Test
//    void findByUsername() {
//    }
//
//    @Test
//    void findById() {
//    }
//
//    @Test
//    void save() {
//    }
//
//    @Test
//    void savePost() {
//    }
//
//    @Test
//    void likePost() {
//    }
}
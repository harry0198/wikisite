package com.harrydrummond.wikisite.api.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserActionsControllerTest {


    @Autowired
    private UserActionsController controller;

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(controller);
    }

//    @Test
//    void saveArticleHandler() {
//    }
//
//    @Test
//    void likeArticleHandler() {
//    }
//
//    @Test
//    void updateUsername() {
//    }

    @Test
    void getUsername() {

    }
}
package com.harrydrummond.projecthjd.api.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
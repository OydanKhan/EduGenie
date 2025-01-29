package com.usyd.edugenie;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class EdugenieApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertNotNull(applicationContext, "Application context should have loaded.");
    }

    @Test
    void testBeanLoading() {
        // Ensure critical beans are loaded, for example, UsersService
        assertNotNull(applicationContext.getBean("usersService"), "UsersService bean should be loaded.");
        assertNotNull(applicationContext.getBean("userLoginService"), "UserLoginService bean should be loaded.");
        assertNotNull(applicationContext.getBean("jwtTokenProvider"), "JwtTokenProvider bean should be loaded.");
    }
}

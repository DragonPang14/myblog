package com.pjl.blog.myblog;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MyblogApplicationTests {

    @Before
    public void init(){
        System.out.println("test start===========");
    }

    @Test
    void contextLoads() {
    }

    @After
    public void destroy(){
        System.out.println("test destroy===============");
    }

}

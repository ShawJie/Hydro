package com.sfan.hydro;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HydroApplicationTests {

    private long start;

    @Before
    public void before(){
        start = System.currentTimeMillis();
    }

    @After
    public void after(){
        System.out.printf("runtime length : %dms\n", System.currentTimeMillis() - start);
    }

    @Test
    public void ConstructorSetting(){
    }

}


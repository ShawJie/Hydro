package com.sfan.hydro;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
@MapperScan("com.sfan.hydro.dao")
public class HydroApplication {

    public static void main(String[] args) {
        SpringApplication.run(HydroApplication.class, args);
    }

}


package com.sfan.hydro;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;

@ServletComponentScan
@MapperScan("com.sfan.hydro.dao")
@SpringBootApplication
public class HydroApplication {

    public static void main(String[] args) {
        SpringApplication.run(HydroApplication.class, args);
    }

}


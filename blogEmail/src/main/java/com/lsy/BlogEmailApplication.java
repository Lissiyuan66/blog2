package com.lsy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
//@EnableAsync
public class BlogEmailApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogEmailApplication.class, args);
    }

}


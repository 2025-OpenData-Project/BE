package com.opendata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OpendataApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpendataApplication.class, args);
    }

}

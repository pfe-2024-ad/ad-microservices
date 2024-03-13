package com.eai.relaunch_service;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableFeignClients(basePackages = "com.eai.openfeignservice")

public class RelaunchServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RelaunchServiceApplication.class, args);
    }

}

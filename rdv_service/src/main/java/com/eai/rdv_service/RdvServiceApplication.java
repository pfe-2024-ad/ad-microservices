package com.eai.rdv_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.eai.openfeignservice")
public class RdvServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RdvServiceApplication.class, args);
    }
}
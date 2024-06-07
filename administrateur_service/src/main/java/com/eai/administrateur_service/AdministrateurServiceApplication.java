package com.eai.administrateur_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
@SpringBootApplication
@EnableFeignClients(basePackages = "com.eai.openfeignservice")
public class AdministrateurServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdministrateurServiceApplication.class, args);
    }
}

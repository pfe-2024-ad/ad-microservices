package com.eai.configuration_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.eai.openfeignservice")
public class ConfigurationServiceApplication {
    public static void main(String[] args) {

        SpringApplication.run(ConfigurationServiceApplication.class, args);
    }


}

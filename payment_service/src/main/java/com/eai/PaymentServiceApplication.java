package com.eai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.eai.openfeignservice")
public class PaymentServiceApplication {
    public static void main(String[] args) {

        SpringApplication.run(PaymentServiceApplication.class, args);

    }
}
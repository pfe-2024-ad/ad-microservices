package com.eai.configuration_service;

import com.eai.configuration_service.model.Param;
import com.eai.configuration_service.repository.ParamRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.eai.openfeignservice")
public class ConfigurationServiceApplication {


        public static void main(String[] args) {
            SpringApplication.run(ConfigurationServiceApplication.class, args);
        }

}

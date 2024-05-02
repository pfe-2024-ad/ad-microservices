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
    private static ParamRepository paramRepository;

    public static void main(String[] args) {

        ConfigurableApplicationContext context =SpringApplication.run(ConfigurationServiceApplication.class, args);

        // Récupération du repository à partir du contexte
        paramRepository = context.getBean(ParamRepository.class);

        insererDonnees();
    }

    private static void insererDonnees() {

        saveParamIfNotExists("SIMILARITY", "80");
        saveParamIfNotExists("NBR_GENERATION", "5");
        saveParamIfNotExists("DATE_EXPIRATION", "15");
        saveParamIfNotExists("MAX_ATTEMPTS", "3");
        saveParamIfNotExists("OTP_LENGTH", "8");
        saveParamIfNotExists("SECRET_KEY_BYTES", "VV3KOX7UQJ4KYAKOHMZPPH3US4CJIMH6F3ZKNB5C2OOBQ6V2KIYHM27Q");
        saveParamIfNotExists("relauch_schedu", "0 0 9 * * ?");

        }

    private static void saveParamIfNotExists(String name, String value) {
        if (paramRepository.findParamByName(name) == null) {
            Param param = new Param();
            param.setName(name);
            param.setValue(value);
            paramRepository.save(param);
        }
    }
    }


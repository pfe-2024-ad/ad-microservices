package com.eai.openfeignservice.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "configuration-service", path = "/agd/configuration-service")
public interface ConfigClient {

    @PostMapping("/get-param")
    ParamDto getParam(@RequestBody ParamDto paramDto);

}

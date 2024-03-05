package com.eai.openfeignservice.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@FeignClient(name = "client-service", path = "/agd/client-service")
public interface UserClient {
    @PostMapping("email")
    Integer saveEmail(@RequestBody ClientRequest request);

    @PostMapping("phone")
    String addPhone(@RequestBody ClientRequest request);


    @PostMapping("check-client")
    Boolean isClientExist(@RequestBody ClientRequest request);

}

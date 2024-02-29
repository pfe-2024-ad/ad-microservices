package com.eai.openfeignservice.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("client-service")
public interface UserClient {
    @PostMapping("/agd/client-service/email")
    Integer saveEmail(@RequestBody ClientRequest request);

    @PostMapping("/agd/client-service/phone")
    String addPhone(@RequestBody ClientRequest request);


    @PostMapping("/agd/client-service/check-client")
    Boolean isClientExist(@RequestBody ClientRequest request);

}

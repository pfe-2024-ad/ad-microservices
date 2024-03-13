package com.eai.openfeignservice.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "client-service", path = "/agd/client-service")
public interface UserClient {
    @PostMapping("create-client")
    Integer saveClient(@RequestBody ClientRequest request);

    @PostMapping("add-client-phone")
    String addPhone(@RequestBody ClientRequest request);


    @PostMapping("check-client-if-exist")
    Boolean isClientExist(@RequestBody ClientRequest request);

    @GetMapping("get-clients-need-relanche")
    List<ClientResponseForRelanche> getClientForRelanche();

}

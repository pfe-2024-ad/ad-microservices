package com.eai.openfeignservice.user;

import com.eai.openfeignservice.notification.EmailSender;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "client-service", path = "/agd/client-service/api")
public interface UserClient {
    @PostMapping("create-client")
    Integer saveClient(@RequestBody ClientRequest request);

    @PostMapping("add-client-phone")
    String addPhone(@RequestBody ClientRequest request);


    @PostMapping("check-client-if-exist")
    Boolean isClientExist(@RequestBody ClientRequest request);

    @GetMapping("get-clients-need-relanche")
    List<ClientResponseForRelanche> getClientForRelanche();

    @PostMapping("get-email-security")
    ClientResponseForSecurity getClientForSecurity(@RequestBody Integer id);

    @PostMapping("send-exist-email")
    String sendEmailExist(@RequestBody EmailSender request);

    @PostMapping("send-email-register")
    String sendEmailRegister(@RequestBody EmailSender request);

    @PostMapping("get-client-step")
    ClientResponseForSecurity getClientStep(@RequestBody ClientRequest request);

    @PostMapping("set-client-step")
    void setClientStep(@RequestBody ClientRequest request);

    @PostMapping("get-client-for-payment")
    ClientResponseForPayment getClientForPayment(@RequestBody Integer id);

}

package com.eai.openfeignservice.notification;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@FeignClient(name = "notification-service", path = "/agd/notification-service")
public interface NotificationClient {

    @PostMapping("send-email")
    String sendEmail(@RequestBody String otpGenerated, String email);

    @PostMapping("send-sms")
    String sendSms(@RequestBody String otpGenerated, String keyPhone, String numPhone);
}

package com.eai.openfeignservice.notification;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("notification-service")
@RequestMapping("/agd/notification-service")

public interface NotificationClient {

    @PostMapping("send-email")
    String sendEmail(@RequestBody EmailSender request);

    @PostMapping("send-sms")
    String sendSms(@RequestBody SmsSender request);
}

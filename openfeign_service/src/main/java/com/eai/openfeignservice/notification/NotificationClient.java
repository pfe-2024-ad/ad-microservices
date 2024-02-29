package com.eai.openfeignservice.notification;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("notification-service")
public interface NotificationClient {

    @PostMapping("/agd/notification-service/send-email")
    String sendEmail(@RequestBody EmailSender request);

    @PostMapping("/agd/notification-service/send-sms")
    String sendSms(@RequestBody SmsSender request);
}

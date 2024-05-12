package com.eai.openfeignservice.notification;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "notification-service", path = "/agd/notification-service")

public interface NotificationClient {

    @PostMapping("send-email")
    String sendOtpEmail(@RequestBody EmailSender request);

    @PostMapping("send-exist-email")
    String sendEmailExist(@RequestBody EmailSender request);

    @PostMapping("send-sms")
    String sendOtpSms(@RequestBody SmsSender request);

    @PostMapping("/send-relaunch-email")
    String sendRelaunchEmail(@RequestBody EmailSender emailSender);

    @PostMapping("send-email-login")
    String sendEmailRegister(@RequestBody EmailSender request);

    @PostMapping("send-contact-email")
    String sendContactEmail(@RequestBody EmailSender request);

}

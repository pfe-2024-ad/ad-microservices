package com.eai.notification_service.controller;

import com.eai.notification_service.service.SmsSenderService;
import com.eai.openfeignservice.notification.SmsSender;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SmsSenderController {
    private final SmsSenderService smsSenderService;

    @PostMapping("/send-sms")
    public String sendSms(@RequestBody SmsSender request) {
        return smsSenderService.sendSms(request.getIndicatifTel(), request.getNumTel(), request.getCodeOtpSms());

    }

}

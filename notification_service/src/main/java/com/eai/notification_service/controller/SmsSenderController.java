package com.eai.notification_service.controller;

import com.eai.notification_service.service.EmailSenderService;
import com.eai.notification_service.service.SmsSenderService;
import com.eai.openfeignservice.notification.SmsSender;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class SmsSenderController {
    private final SmsSenderService smsSenderService;

    @PostMapping("/send-sms")
    public String sendSms(@RequestBody SmsSender request) {
        smsSenderService.sendSms(request.getIndicatifTel(), request.getNumTel(), request.getCodeOtpSms());
        return "Sms envoyé avec succès à " + request.getIndicatifTel()+request.getNumTel()+ " avec le code OTP : " + request.getCodeOtpSms();
    }

}

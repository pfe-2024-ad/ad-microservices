package com.eai.notification_service.controller;

import com.eai.notification_service.service.RelaunchEmailSenderService;
import com.eai.openfeignservice.notification.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RelaunchEmailSenderController {
    private final RelaunchEmailSenderService relaunchEmailSenderService;

    @PostMapping("/send-relaunch-email")
    public String sendRelaunchEmail(@RequestBody EmailSender emailSender) {
        return relaunchEmailSenderService.sendEmailRelaunch(emailSender);
    }
}

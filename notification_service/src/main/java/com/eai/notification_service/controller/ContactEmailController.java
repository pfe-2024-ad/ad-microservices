package com.eai.notification_service.controller;

import com.eai.notification_service.service.ContactEmailService;
import com.eai.openfeignservice.notification.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ContactEmailController {
    private final ContactEmailService contactEmailService ;

    @PostMapping("/send-contact-email")
    public String sendContactEmail(@RequestBody EmailSender emailSender) {
        return contactEmailService.sendContactEmail(emailSender);
    }
}


package com.eai.notification_service.controller;

import com.eai.notification_service.service.EmailSenderService;
import com.eai.openfeignservice.notification.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class EmailSenderController {

    private final EmailSenderService emailSenderService;

   @PostMapping("/send-email")
   public String sendEmail(@RequestBody EmailSender request) {
       return emailSenderService.sendEmail(request.getEmail(), request.getSubject(), request.getVariables(), request.getCheminTemplate());

   }
}

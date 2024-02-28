package com.eai.notification_service.controller;

import com.eai.notification_service.service.EmailSenderService;
import com.eai.openfeignservice.notification.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class EmailSenderController {

    private final EmailSenderService emailSenderService;

   @PostMapping("/send-email")
   public String sendEmail(@RequestBody EmailSender request) {
       emailSenderService.sendEmail(request.getEmail(),   request.getSubject(), request.getVariables(), request.getCheminTemplate());
       return "Email envoyé avec succès à " + request.getEmail()+ " avec le code OTP : " + request.getCodeOtpEmail() + "  et avec le secretKey " + request.getSecretKey();
   }
}

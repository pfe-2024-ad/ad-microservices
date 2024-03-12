package com.eai.notification_service.controller;

import com.eai.notification_service.service.EmailSenderService;
import com.eai.openfeignservice.notification.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SecurityServiceEmailController {

    private final EmailSenderService emailSenderService;

   @PostMapping("/send-email")
   public String sendOtpEmail(@RequestBody EmailSender request) {
       String subject = "Agence Directe – Ouverture de compte en ligne";
       String templatePath = "otpService/email/send-otp-email-template.html";
       Map<String, Object> variables = new HashMap<>();
       variables.put("codeOtpEmail", request.getCodeOtpEmail());
       variables.put("subject", subject);

       return emailSenderService.sendOtpEmail(request.getEmail(), subject, variables, templatePath);
   }
}

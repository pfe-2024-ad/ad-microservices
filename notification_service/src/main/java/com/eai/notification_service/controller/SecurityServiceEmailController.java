package com.eai.notification_service.controller;

import com.eai.notification_service.service.DemandeService;
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
    private final DemandeService demandeService;


   @PostMapping("/send-email")
   public String sendOtpEmail(@RequestBody EmailSender request) {

       String subject = "Agence Directe – Ouverture de compte en ligne";
       String templatePath = "otpService/email/send-otp-email-template.html";

       Map<String, Object> variables = new HashMap<>();
       variables.put("codeOtpEmail", request.getCodeOtpEmail());
       variables.put("subject", subject);

       return emailSenderService.sendOtpEmail(request.getEmail(), subject, variables, templatePath);
   }

    @PostMapping("/send-exist-email")
    public String sendEmailExist(@RequestBody EmailSender emailSender) {
        return demandeService.sendEmailExist(emailSender);
    }

    @PostMapping("/send-email-login")
    public String sendEmailRegister(@RequestBody EmailSender emailSender) {
        return demandeService.sendEmailRegister(emailSender);
    }
}

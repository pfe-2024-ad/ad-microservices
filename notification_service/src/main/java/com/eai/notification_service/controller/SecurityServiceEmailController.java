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

    @PostMapping("/send-exist-email")
    public String sendEmailExist(@RequestBody EmailSender request){
        String subject = "Agence directe : Avez-vous de la difficulté à ouvrir une session dans votre compte?";
        String templatePath = "otpService/email/send-exist-email-template.html";

        Map<String, Object> variables = new HashMap<>();
        variables.put("codeOtpEmail", request.getCodeOtpEmail());
        variables.put("subject", subject);

        return emailSenderService.sendOtpEmail(request.getEmail(), subject, variables, templatePath);
    }

    @PostMapping("/send-email-login")
    public String sendEmailRegister(@RequestBody EmailSender request){
        String subject = "Agence directe : Avez-vous de la difficulté à ouvrir une session dans votre compte?";
        String templatePath = "otpService/email/send-login-template.html";

        Map<String, Object> variables = new HashMap<>();
        variables.put("subject", subject);

        return emailSenderService.sendOtpEmail(request.getEmail(), subject, variables, templatePath);    }
}

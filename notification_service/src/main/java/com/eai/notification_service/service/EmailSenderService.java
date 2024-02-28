package com.eai.notification_service.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service()
@RequiredArgsConstructor
@Slf4j
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ThymeleafService thymeleafService;

    //private String fromEmail="BankOfAfrique@gmail.com";

    public void sendEmail(String toEmail, String subject, Map<String, Object> variables, String cheminTemplate){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );
            //helper.setFrom(fromEmail);
            helper.setTo(toEmail);

            helper.setSubject(subject);


            helper.setText(thymeleafService.createContent(cheminTemplate, variables), true);

            mailSender.send(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

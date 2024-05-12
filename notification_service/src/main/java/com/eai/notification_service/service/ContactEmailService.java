package com.eai.notification_service.service;

import com.eai.notification_service.outils.enums.EmailStatus;
import com.eai.openfeignservice.notification.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactEmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ThymeleafService thymeleafService;

    public String sendContactEmail(EmailSender emailSender){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );
            String subject = "Demande de contact";
            String cheminTemplate = "contactService.email/send-contact-email-template.html";


            Map<String, Object> variables = new HashMap<>();
            variables.put("object", subject);

            helper.setTo("ibtissame.arrai@gmail.com");

            helper.setSubject(subject);


            helper.setText(thymeleafService.createContent(cheminTemplate, variables), true);

            mailSender.send(message);

            return EmailStatus.SUCCESSFUL.getLabel();
        }catch (Exception e){
            e.printStackTrace();
            return EmailStatus.ERROR.getLabel();
        }
    }
}

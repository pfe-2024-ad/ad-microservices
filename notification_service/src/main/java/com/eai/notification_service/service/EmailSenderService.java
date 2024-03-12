package com.eai.notification_service.service;

import com.eai.notification_service.outils.enums.EmailStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service()
@RequiredArgsConstructor
@Slf4j
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ThymeleafService thymeleafService;


    public void sendOtpEmail(String toEmail, String subject, Map<String, Object> variables, String templatePath){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            helper.setTo(toEmail);

            helper.setSubject(subject);


            helper.setText(thymeleafService.createContent(templatePath, variables), true);

            mailSender.send(message);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

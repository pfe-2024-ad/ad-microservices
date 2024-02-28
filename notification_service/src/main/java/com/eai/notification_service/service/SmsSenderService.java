package com.eai.notification_service.service;

import com.eai.notification_service.config.TwilioConfiguration;
import com.eai.openfeignservice.notification.SmsSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service("twilio")
@RequiredArgsConstructor
@Slf4j
public class SmsSenderService {

    private final TwilioConfiguration twilioConfiguration;


    public void sendSms(String indicatifTel, String numTel, String codeOtpSms) {
        String phoneNumber = indicatifTel + numTel;
        PhoneNumber to = new PhoneNumber(phoneNumber);
        PhoneNumber from = new PhoneNumber(twilioConfiguration.getTrialNumber());

        String message = "Agence Directe. Ouverture de compte en ligne Code de confirmation " + codeOtpSms + " valable pour une durée de: 15 minutes";

        MessageCreator creator = Message.creator(to, from, message);
        creator.create();

    }

}

package com.eai.securityservice.controller;

import com.eai.openfeignservice.notification.NotificationClient;
import com.eai.openfeignservice.notification.SmsSender;
import com.eai.openfeignservice.user.ClientRequest;
import com.eai.openfeignservice.user.UserClient;
import com.eai.securityservice.dto.OtpPhoneRequest;
import com.eai.securityservice.model.Counter;
import com.eai.securityservice.model.History;
import com.eai.securityservice.model.Otp;
import com.eai.securityservice.repository.CounterRepository;
import com.eai.securityservice.repository.HistoryRepository;
import com.eai.securityservice.repository.OtpRepository;
import com.eai.securityservice.service.OtpCompareService;
import com.eai.securityservice.service.OtpGenerateService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/otp_phone")
@RequiredArgsConstructor
public class OtpPhoneController {

    private final OtpGenerateService otpGenerateService;
    private final OtpCompareService otpCompareService;
    private final OtpRepository otpRepository;
    private final HistoryRepository historyRepository;
    private final Counter counter;
    private final CounterRepository counterRepository;

    private final NotificationClient notificationClient;
    private final UserClient userClient;

    @PostMapping("/generate")
    public String generatePhoneOtp(@RequestBody OtpPhoneRequest otpPhoneRequest) {
        String generatedOtp = otpGenerateService.generateOtp(counter.getCounter());
        History history = historyRepository.findTopByKeyPhoneAndNumPhoneOrderByDateGenerationDesc(otpPhoneRequest.getKeyPhone(), otpPhoneRequest.getNumPhone());
        Otp otp = otpRepository.findByIdClient(otpPhoneRequest.getIdClient());
        SmsSender smsSender = SmsSender.builder()
                .indicatifTel(otpPhoneRequest.getKeyPhone())
                .numTel(otpPhoneRequest.getNumPhone())
                .codeOtpSms(generatedOtp)
                .build();

            if (history == null) {
                history = new History(otpPhoneRequest.getKeyPhone(), otpPhoneRequest.getNumPhone(), counter.getCounter(), new Date());

                    otp.setCounter(counter.getCounter());
                    otp.setDateGeneration(new Date());
                    otp.setAttempts(0);
                   otp.setKeyPhone(otpPhoneRequest.getKeyPhone());
                   otp.setNumPhone(otpPhoneRequest.getNumPhone());
                    otpRepository.save(otp);
                    historyRepository.save(history);
                    counter.incrementCounter();
                    counterRepository.save(counter);
                return notificationClient.sendSms(smsSender);


            } else{
                if (history.getNumGeneration() < 5) {
                    history.setCounter(counter.getCounter());
                    history.setDateGeneration(new Date());
                    history.incrementNumGeneration();
                    otp.setCounter(counter.getCounter());
                    otp.setKeyPhone(otpPhoneRequest.getKeyPhone());
                    otp.setNumPhone(otpPhoneRequest.getNumPhone());
                    otp.setDateGeneration(new Date());
                    otp.setAttempts(0);

                    otpRepository.save(otp);
                    historyRepository.save(history);
                    counter.incrementCounter();
                    counterRepository.save(counter);

                    return notificationClient.sendSms(smsSender);



                } else if (history.getNumGeneration() >= 5 && isPast30Minutes(history.getDateGeneration()) > 1) {
                    history = new History(otpPhoneRequest.getKeyPhone(), otpPhoneRequest.getNumPhone(), counter.getCounter(), new Date());
                    otp.setCounter(counter.getCounter());
                    otp.setDateGeneration(new Date());
                    otp.setAttempts(0);
                    otp.setKeyPhone(otpPhoneRequest.getKeyPhone());
                    otp.setNumPhone(otpPhoneRequest.getNumPhone());

                    otpRepository.save(otp);
                    historyRepository.save(history);
                    counter.incrementCounter();
                    counterRepository.save(counter);

                    return notificationClient.sendSms(smsSender);

                }
                else {
                    return "error, Veuillez réessayer plus tard. Vous avez dépassé le nombre maximal de générations d'OTP autorisées (5) ou la dernière tentative était il y a moins de 30 minutes.";
                }
            }
    }

    private long isPast30Minutes(Date date) {
        long diffInMilliseconds = new Date().getTime() - date.getTime();
        return TimeUnit.MILLISECONDS.toMinutes(diffInMilliseconds);
    }


    @PostMapping("/compare")
    public String compareOtp(@RequestBody OtpPhoneRequest otpPhoneRequest) {
        // Use the counter value to verify the OTP

        Otp otp = otpRepository.findByKeyPhoneAndNumPhone(otpPhoneRequest.getKeyPhone(), otpPhoneRequest.getNumPhone());



        if (isPast30Minutes(otp.getDateGeneration())<30) {
            if (otp.getAttempts()<3) {
                String isOtpValid = otpCompareService.verifyOtp(otpPhoneRequest.getUserInput(), otp.getCounter());
                ClientRequest client = ClientRequest.builder()
                        .idClient(otpPhoneRequest.getIdClient())
                        .indicatifTel(otpPhoneRequest.getKeyPhone())
                        .numTel(otpPhoneRequest.getNumPhone())
                        .build();

                otp.incrementAttempt();
                otpRepository.save(otp);
                return userClient.addPhone(client);
            }
            else{
                return "OTP expired";
            }
        }
        else{
            return "otp time out";
        }

    }
}

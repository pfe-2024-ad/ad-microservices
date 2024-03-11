package com.eai.securityservice.controller;

import com.eai.openfeignservice.notification.NotificationClient;
import com.eai.openfeignservice.notification.SmsSender;
import com.eai.openfeignservice.user.UserClient;
import com.eai.securityservice.dto.OtpPhoneRequest;
import com.eai.securityservice.model.Counter;
import com.eai.securityservice.model.History;
import com.eai.securityservice.model.Otp;
import com.eai.securityservice.outiles.enums.OtpGenerationStatusEnum;
import com.eai.securityservice.outiles.enums.StatusOTP;
import com.eai.securityservice.repository.CounterRepository;
import com.eai.securityservice.repository.HistoryRepository;
import com.eai.securityservice.repository.OtpRepository;
import com.eai.securityservice.service.OtpCompareService;
import com.eai.securityservice.service.OtpGenerateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
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

    @PostMapping("/generate")
    public String generatePhoneOtp(@RequestBody OtpPhoneRequest otpPhoneRequest) {
        String generatedOtp = otpGenerateService.generateOtp(counter.getCounter());
        History history = historyRepository.findTopByKeyPhoneAndNumPhoneOrderByDateGenerationDesc(otpPhoneRequest.getKeyPhone(), otpPhoneRequest.getNumPhone());
        Otp otp = otpRepository.findByIdClient(otpPhoneRequest.getIdClient());
        SmsSender smsSender = SmsSender.builder()
                .keyPhone(otpPhoneRequest.getKeyPhone())
                .numPhone(otpPhoneRequest.getNumPhone())
                .codeOtpSms(generatedOtp)
                .build();
        String isSent;
            if (history == null) {
                history = new History(otpPhoneRequest.getKeyPhone(), otpPhoneRequest.getNumPhone(), counter.getCounter(), new Date());
                otp.setCounter(counter.getCounter());
                otp.setDateGeneration(new Date());
                otp.setAttempts(0);
                otp.setKeyPhone(otpPhoneRequest.getKeyPhone());
                otp.setNumPhone(otpPhoneRequest.getNumPhone());
                notificationClient.sendOtpSms(smsSender);
                isSent = OtpGenerationStatusEnum.SUCCESS.getLabel();

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
                    notificationClient.sendOtpSms(smsSender);
                    isSent = OtpGenerationStatusEnum.SUCCESS.getLabel();



                } else if (history.getNumGeneration() == 5 && isPast30Minutes(history.getDateGeneration()) > 30) {
                    history = new History(otpPhoneRequest.getKeyPhone(), otpPhoneRequest.getNumPhone(), counter.getCounter(), new Date());
                    otp.setCounter(counter.getCounter());
                    otp.setDateGeneration(new Date());
                    otp.setAttempts(0);
                    otp.setKeyPhone(otpPhoneRequest.getKeyPhone());
                    otp.setNumPhone(otpPhoneRequest.getNumPhone());
                    notificationClient.sendOtpSms(smsSender);
                    isSent = OtpGenerationStatusEnum.MAX_GENERATED_OTP_ERROR.getLabel();
                }
                else {
                    isSent = OtpGenerationStatusEnum.EMAIL_EXIST_ERROR.getLabel();
                }
            }
        otpRepository.save(otp);
        historyRepository.save(history);
        counter.incrementCounter();
        counterRepository.save(counter);
        return isSent;
    }

    private long isPast30Minutes(Date date) {
        long diffInMilliseconds = new Date().getTime() - date.getTime();
        return TimeUnit.MILLISECONDS.toMinutes(diffInMilliseconds);
    }


    @PostMapping("/compare")
    public StatusOTP compareOtp(@RequestBody OtpPhoneRequest otpPhoneRequest) {
        // Use the counter value to verify the OTP

        Otp otp = otpRepository.findByKeyPhoneAndNumPhone(otpPhoneRequest.getKeyPhone(), otpPhoneRequest.getNumPhone());
        if (isPast30Minutes(otp.getDateGeneration())<30) {
            if (otp.getAttempts()<3) {
                Boolean isOtpValid = otpCompareService.verifyOtp(otpPhoneRequest.getUserInput(), otp.getCounter());
                otp.incrementAttempt();
                otpRepository.save(otp);
                if (isOtpValid) {
                    return StatusOTP.VALIDE;
                } else {
                    return StatusOTP.INVALID;
                }
            } else {
                return StatusOTP.EXPIRED;
            }
        } else {
            return StatusOTP.TIMEOUT;
        }

    }
}

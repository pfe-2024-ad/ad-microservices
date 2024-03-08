package com.eai.securityservice.controller;

import com.eai.openfeignservice.notification.NotificationClient;
import com.eai.openfeignservice.user.ClientRequest;
import com.eai.openfeignservice.user.UserClient;
import com.eai.securityservice.dto.OtpEmailRequest;
import com.eai.securityservice.model.*;
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
@RequestMapping("/otp_email")
@RequiredArgsConstructor
public class OtpEmailController {

    private final OtpGenerateService otpGenerateService;
    private final OtpCompareService otpCompareService;
    private final OtpRepository otpRepository;
    private final HistoryRepository historyRepository;
    private final Counter counter;
    private final CounterRepository counterRepository;
    private final NotificationClient notificationClient;
    private final UserClient userClient;

    @PostMapping("/generate")
    public String generateEmailOtp(@RequestBody OtpEmailRequest otpEmailRequest ) {

      ClientRequest client = ClientRequest.builder()
                .email(otpEmailRequest.getEmail())
                .build();
      if(!userClient.isClientExist(client)) {
          String generatedOtp = otpGenerateService.generateOtp(counter.getCounter());
          Otp otp = otpRepository.findByEmail(otpEmailRequest.getEmail());
          History history = historyRepository.findTopByEmailOrderByDateGenerationDesc(otpEmailRequest.getEmail());
          String isSent;

          if (otp == null) {
              history = new History(otpEmailRequest.getEmail(), counter.getCounter(), new Date());
              otp = new Otp(otpEmailRequest.getEmail(), counter.getCounter(), new Date());
              notificationClient.sendEmail(otpEmailRequest.getEmail(), generatedOtp);
              isSent = OtpGenerationStatusEnum.SUCCESS.getLabel();
          } else {
              if (history.getNumGeneration() < 5) {
                  history.setCounter(counter.getCounter());
                  history.setDateGeneration(new Date());
                  history.incrementNumGeneration();
                  otp.setCounter(counter.getCounter());
                  otp.setDateGeneration(new Date());
                  otp.setAttempts(0);
                  notificationClient.sendEmail(otpEmailRequest.getEmail(), generatedOtp);
                  isSent = OtpGenerationStatusEnum.SUCCESS.getLabel();

              } else if (history.getNumGeneration() == 5 && isPast30Minutes(history.getDateGeneration()) > 1) {
                  history = new History(otpEmailRequest.getEmail(), counter.getCounter(), new Date());
                  otp.setCounter(counter.getCounter());
                  otp.setDateGeneration(new Date());
                  otp.setAttempts(0);
                  notificationClient.sendEmail(otpEmailRequest.getEmail(), generatedOtp);
                  isSent = OtpGenerationStatusEnum.SUCCESS.getLabel();


              } else {
                  isSent = OtpGenerationStatusEnum.MAX_GENERATED_OTP_ERROR.getLabel();
              }
          }

        otpRepository.save(otp);
        historyRepository.save(history);
        counter.incrementCounter();
        counterRepository.save(counter);
        return isSent;
    }else{
        return OtpGenerationStatusEnum.EMAIL_EXIST_ERROR.getLabel();
        }
    }

    private long isPast30Minutes(Date date) {
        long diffInMilliseconds = new Date().getTime() - date.getTime();
        return TimeUnit.MILLISECONDS.toMinutes(diffInMilliseconds);
    }
    @PostMapping("/compare")
    public String compareOtp(@RequestBody OtpEmailRequest otpEmailRequest) {
            // Use the counter value to verify the OTP
        Otp otp = otpRepository.findByEmail(otpEmailRequest.getEmail());
        if (isPast30Minutes(otp.getDateGeneration()) < 15) {
                if (otp.getAttempts() < 3) {
                    Boolean isOtpValid = otpCompareService.verifyOtp(otpEmailRequest.getUserInput(), otp.getCounter());

                    ClientRequest client = ClientRequest.builder()
                            .email(otpEmailRequest.getEmail())
                            .build();

                    Integer idClient = userClient.saveEmail(client);
                    otp.incrementAttempt();
                    otp.setIdClient(idClient);
                    otpRepository.save(otp);

                    if (isOtpValid) {
                        return StatusOTP.VALIDE.getLabel();
                    }else {
                        return StatusOTP.INVALID.getLabel();
                    }
                } else{
                    return StatusOTP.EXPIRED.getLabel();
                }
            } else {
              return StatusOTP.TIMEOUT.getLabel();
            }
    }
}


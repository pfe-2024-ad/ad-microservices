package com.eai.securityservice.controller;

import com.eai.openfeignservice.notification.EmailSender;
import com.eai.openfeignservice.notification.NotificationClient;
import com.eai.openfeignservice.user.ClientRequest;
import com.eai.openfeignservice.user.UserClient;
import com.eai.securityservice.dto.OtpEmailRequest;
import com.eai.securityservice.model.Counter;
import com.eai.securityservice.model.History;
import com.eai.securityservice.model.Otp;
import com.eai.securityservice.repository.CounterRepository;
import com.eai.securityservice.repository.HistoryRepository;
import com.eai.securityservice.repository.OtpRepository;
import com.eai.securityservice.service.OtpCompareService;
import com.eai.securityservice.service.OtpGenerateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
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
    public String generatePhoneOtp(@RequestBody OtpEmailRequest otpEmailRequest ) {
      ClientRequest client = ClientRequest.builder()
                .email(otpEmailRequest.getEmail())
                .build();
      if(!userClient.isClientExist(client)) {
          String generatedOtp = otpGenerateService.generateOtp(counter.getCounter());

          Otp otp = otpRepository.findByEmail(otpEmailRequest.getEmail());
          History history = historyRepository.findTopByEmailOrderByDateGenerationDesc(otpEmailRequest.getEmail());
          Map<String, Object> responseMap = new HashMap<>();

          String subject = "Agence Directe – Ouverture de compte en ligne";
          String cheminTemplate = "otpService/email/send-otp-email-template.html";


          Map<String, Object> variables = new HashMap<>();
          variables.put("codeOtpEmail", generatedOtp);
          variables.put("subject", subject);

          EmailSender emailSender = EmailSender.builder()
                  .email(otpEmailRequest.getEmail())
                  .codeOtpEmail(generatedOtp)
                  .subject(subject)
                  .variables(variables)
                  .cheminTemplate(cheminTemplate)
                  .build();


          if (otp == null) {
              history = new History(otpEmailRequest.getEmail(), counter.getCounter(), new Date());
              otp = new Otp(otpEmailRequest.getEmail(), counter.getCounter(), new Date());

              otpRepository.save(otp);
              historyRepository.save(history);
              counter.incrementCounter();
              counterRepository.save(counter);

              return notificationClient.sendEmail(emailSender);
          } else {
              if (history.getNumGeneration() < 5) {
                  history.setCounter(counter.getCounter());
                  history.setDateGeneration(new Date());
                  history.incrementNumGeneration();
                  otp.setCounter(counter.getCounter());
                  otp.setDateGeneration(new Date());
                  otp.setAttempts(0);

                  otpRepository.save(otp);
                  historyRepository.save(history);
                  counter.incrementCounter();
                  counterRepository.save(counter);

                  return notificationClient.sendEmail(emailSender);
              } else if (history.getNumGeneration() >= 5 && isPast30Minutes(history.getDateGeneration()) > 1) {
                  history = new History(otpEmailRequest.getEmail(), counter.getCounter(), new Date());
                  otp.setCounter(counter.getCounter());
                  otp.setDateGeneration(new Date());
                  otp.setAttempts(0);

                  otpRepository.save(otp);
                  historyRepository.save(history);
                  counter.incrementCounter();
                  counterRepository.save(counter);

                  return notificationClient.sendEmail(emailSender);


              } else {
                  return "error, Veuillez réessayer plus tard. Vous avez dépassé le nombre maximal de générations d'OTP autorisées (5) ou la dernière tentative était il y a moins de 30 minutes.";
              }
          }
      }else{
          return "error, Cette adresse email est déjà utilisée.";
      }
    }

    private long isPast30Minutes(Date date) {
        long diffInMilliseconds = new Date().getTime() - date.getTime();
        return TimeUnit.MILLISECONDS.toMinutes(diffInMilliseconds);
    }
    @PostMapping("/compare")
    public ResponseEntity<Map<String, Object>> compareOtp(@RequestBody OtpEmailRequest otpEmailRequest) {

            // Use the counter value to verify the OTP

            Otp otp = otpRepository.findByEmail(otpEmailRequest.getEmail());
            Map<String, Object> responseMap = new HashMap<>();

        if (isPast30Minutes(otp.getDateGeneration()) < 10) {
                if (otp.getAttempts() < 3) {
                    String isOtpValid = otpCompareService.verifyOtp(otpEmailRequest.getUserInput(), otp.getCounter());

                    ClientRequest client = ClientRequest.builder()
                            .email(otpEmailRequest.getEmail())
                            .build();


                    Integer idClient = userClient.saveEmail(client);
                    otp.incrementAttempt();

                    otp.setIdClient(idClient);

                    otpRepository.save(otp);
                    responseMap.put(isOtpValid, idClient);
                    return ResponseEntity.ok(responseMap);
                } else {
                    responseMap.put("error2345", "OTP expired");
                    return ResponseEntity.ok(responseMap);
                }
            } else {
                responseMap.put("error2345", "otp time out");
                return ResponseEntity.ok(responseMap);
            }
    }
}


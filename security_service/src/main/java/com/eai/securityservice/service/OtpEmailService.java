package com.eai.securityservice.service;

import com.bastiaanjansen.otp.HMACAlgorithm;
import com.bastiaanjansen.otp.HOTPGenerator;
import com.eai.openfeignservice.notification.EmailSender;
import com.eai.openfeignservice.notification.NotificationClient;
import com.eai.openfeignservice.user.ClientRequest;
import com.eai.securityservice.dto.OtpEmailRequest;
import com.eai.securityservice.model.Counter;
import com.eai.securityservice.model.History;
import com.eai.securityservice.model.Otp;
import com.eai.securityservice.outiles.enums.OtpGenerationStatusEnum;
import com.eai.securityservice.outiles.enums.StatusOTP;
import com.eai.securityservice.repository.CounterRepository;
import com.eai.securityservice.repository.HistoryRepository;
import com.eai.securityservice.repository.OtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
public class OtpEmailService {
    private final OtpRepository otpRepository;
    private final HistoryRepository historyRepository;
    private final Counter counter;
    private final CounterRepository counterRepository;
    private final NotificationClient notificationClient;

    private static final byte[] SECRET_KEY_BYTES = "VV3KOX7UQJ4KYAKOHMZPPH3US4CJIMH6F3ZKNB5C2OOBQ6V2KIYHM27Q".getBytes();

    public String generateOtpEmail(@RequestBody OtpEmailRequest otpEmailRequest ) {


        String generatedOtp = generateOtp(counter.getCounter());
        Otp otp = otpRepository.findByEmail(otpEmailRequest.getEmail());
        History history = historyRepository.findTopByEmailOrderByDateGenerationDesc(otpEmailRequest.getEmail());
        String isSent;
        EmailSender emailSender = EmailSender.builder()
                .email(otpEmailRequest.getEmail())
                .codeOtpEmail(generatedOtp)
                .build();
        if (otp == null) {
            history = new History(otpEmailRequest.getEmail(), counter.getCounter(), new Date());
            otp = new Otp(otpEmailRequest.getEmail(), counter.getCounter(), new Date());
            isSent =  notificationClient.sendOtpEmail(emailSender);
        } else {
            if (history.getNumGeneration() < 5) {
                history.setCounter(counter.getCounter());
                history.setDateGeneration(new Date());
                history.incrementNumGeneration();
                otp.setCounter(counter.getCounter());
                otp.setDateGeneration(new Date());
                otp.setAttempts(0);
                isSent =  notificationClient.sendOtpEmail(emailSender);

            } else if (history.getNumGeneration() == 5 && isPast30Minutes(history.getDateGeneration()) > 1) {
                history = new History(otpEmailRequest.getEmail(), counter.getCounter(), new Date());
                otp.setCounter(counter.getCounter());
                otp.setDateGeneration(new Date());
                otp.setAttempts(0);
                isSent =  notificationClient.sendOtpEmail(emailSender);


            } else {
                isSent = OtpGenerationStatusEnum.MAX_GENERATED_OTP_ERROR.getLabel();
            }
        }

        otpRepository.save(otp);
        historyRepository.save(history);
        counter.incrementCounter();
        counterRepository.save(counter);
        return isSent;
    }

    public String compareOtp(@RequestBody ClientRequest otpEmailRequest) {

        Otp otp = otpRepository.findByEmail(otpEmailRequest.getEmail());
        if (isPast30Minutes(otp.getDateGeneration()) < 15) {
            if (otp.getAttempts() < 3) {
                Boolean isOtpValid = verifyOtp(otpEmailRequest.getUserInput(), otp.getCounter());


                Integer idClient = userClient.saveEmail(otpEmailRequest);
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

    public boolean compareOtp( String userInput , Integer counter) {
        HOTPGenerator hotp = new HOTPGenerator.Builder(SECRET_KEY_BYTES)
                .withPasswordLength(8)
                .withAlgorithm(HMACAlgorithm.SHA256)
                .build();
        return hotp.verify(userInput, counter);
    }




    public Boolean verifyOtp(String input, Integer counter){
        return compareOtp(input, counter);
    }


    private long isPast30Minutes(Date date) {
        long diffInMilliseconds = new Date().getTime() - date.getTime();
        return TimeUnit.MILLISECONDS.toMinutes(diffInMilliseconds);
    }

    public String generateOtp(Integer counter) {

        HOTPGenerator hotp = new HOTPGenerator.Builder(SECRET_KEY_BYTES)
                .withPasswordLength(8)
                .withAlgorithm(HMACAlgorithm.SHA256)
                .build();
        String code = hotp.generate(counter);

        return code;

    }


}

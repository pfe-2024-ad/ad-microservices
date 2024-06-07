package com.eai.securityservice.service;

import com.bastiaanjansen.otp.HMACAlgorithm;
import com.bastiaanjansen.otp.HOTPGenerator;
import com.eai.openfeignservice.config.ConfigClient;
import com.eai.openfeignservice.config.ParamDto;
import com.eai.openfeignservice.notification.NotificationClient;
import com.eai.openfeignservice.notification.SmsSender;
import com.eai.openfeignservice.user.ClientRequest;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
@Service
@RequiredArgsConstructor
public class OtpPhoneService {

    private final OtpRepository otpRepository;
    private final HistoryRepository historyRepository;
    private final Counter counter;
    private final CounterRepository counterRepository;
    private final NotificationClient notificationClient;
    private final UserClient userClient;

    private final ConfigClient securityConfigClient;



    public String generateOtpPhone(@RequestBody OtpPhoneRequest otpPhoneRequest) {
        //get DATE_EXPIRATION from config service
        ParamDto paramDto1 = ParamDto.builder()
                .name("DATE_EXPIRATION")
                .build();
        Integer DATE_EXPIRATION_CONFIG = Integer.parseInt(securityConfigClient.getParam(paramDto1).getValue());

        ParamDto paramDto2 = ParamDto.builder()
                .name("NEW_DATE_GENERATION")
                .build();
        Integer NEW_DATE_GENERATION_CONFIG = Integer.parseInt(securityConfigClient.getParam(paramDto2).getValue());


        String generatedOtp = generateOtp(counter.getCounter());
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
            if(Objects.equals(notificationClient.sendOtpSms(smsSender), "01")){
                isSent =  OtpGenerationStatusEnum.SUCCESS.getLabel();
            }else{
                isSent =  OtpGenerationStatusEnum.EMAIL_ERROR.getLabel();
            }

        } else{
            //get maxNbrGeneration from config service
            ParamDto paramDto3 = ParamDto.builder()
                    .name("NBR_GENERATION")
                    .build();
            Integer NBR_GENERATION_CONFIG = Integer.parseInt(securityConfigClient.getParam(paramDto3).getValue());

            if (history.getNumGeneration() < NBR_GENERATION_CONFIG) {
                history.setCounter(counter.getCounter());
                history.setDateGeneration(new Date());
                history.incrementNumGeneration();
                otp.setCounter(counter.getCounter());
                otp.setKeyPhone(otpPhoneRequest.getKeyPhone());
                otp.setNumPhone(otpPhoneRequest.getNumPhone());
                otp.setDateGeneration(new Date());
                otp.setAttempts(0);
                if(Objects.equals(notificationClient.sendOtpSms(smsSender), "01")){
                    isSent =  OtpGenerationStatusEnum.SUCCESS.getLabel();
                }else{
                    isSent =  OtpGenerationStatusEnum.EMAIL_ERROR.getLabel();
                }



            } else if ( isPast30Minutes(history.getDateGeneration()) >  NEW_DATE_GENERATION_CONFIG) {
                history = new History(otpPhoneRequest.getKeyPhone(), otpPhoneRequest.getNumPhone(), counter.getCounter(), new Date());
                otp.setCounter(counter.getCounter());
                otp.setDateGeneration(new Date());
                otp.setAttempts(0);
                if(Objects.equals(notificationClient.sendOtpSms(smsSender), "01")){
                    isSent =  OtpGenerationStatusEnum.SUCCESS.getLabel();
                }else{
                    isSent =  OtpGenerationStatusEnum.EMAIL_ERROR.getLabel();
                }
            }
            else {
                isSent = OtpGenerationStatusEnum.MAX_GENERATED_OTP_ERROR.getLabel();
            }
        }
        otpRepository.save(otp);
        historyRepository.save(history);
        counter.incrementCounter();
        counterRepository.save(counter);
        return isSent;
    }
    public String compareOtp(@RequestBody OtpPhoneRequest otpPhoneRequest) {

        Otp otp = otpRepository.findByKeyPhoneAndNumPhone(otpPhoneRequest.getKeyPhone(), otpPhoneRequest.getNumPhone());

        //get DATE_EXPIRATION from config service
        ParamDto paramDto = ParamDto.builder()
                .name("DATE_EXPIRATION")
                .build();
        Integer DATE_EXPIRATION_CONFIG = Integer.parseInt(securityConfigClient.getParam(paramDto).getValue());

        if (isPast30Minutes(otp.getDateGeneration())<DATE_EXPIRATION_CONFIG) {
            //get maxAttemps from config service
            ParamDto paramDto1 = ParamDto.builder()
                    .name("MAX_ATTEMPTS")
                    .build();
            Integer MAX_ATTEMPTS_CONFIG = Integer.parseInt(securityConfigClient.getParam(paramDto1).getValue());
            if (otp.getAttempts()<MAX_ATTEMPTS_CONFIG) {
                Boolean isOtpValid = verifyOtp(otpPhoneRequest.getUserInput(), otp.getCounter());
                otp.incrementAttempt();
                otpRepository.save(otp);
                if (isOtpValid) {
                    ClientRequest clientRequest = ClientRequest.builder()
                            .idClient(otpPhoneRequest.getIdClient())
                            .indicatifTel(otpPhoneRequest.getKeyPhone())
                            .numTel(otpPhoneRequest.getNumPhone()).build();
                    String idClient = userClient.addPhone(clientRequest);
                    return StatusOTP.VALID.getLabel();
                } else {
                    return StatusOTP.INVALID.getLabel();
                }
            } else {
                return StatusOTP.EXPIRED_ATTEMPT.getLabel();
            }
        } else {
            return StatusOTP.TIMEOUT.getLabel();
        }

    }







    public boolean compareOtp( String userInput , Integer counter) {

        //GET OTP_LENGTH from configuration service
        ParamDto paramDto1 = ParamDto.builder()
                .name("OTP_LENGTH")
                .build();
        Integer OTP_LENGTH_CONFIG = Integer.parseInt(securityConfigClient.getParam(paramDto1).getValue());

        //GET SECRET_KEY_BYTES from configuration service
        ParamDto paramDto2 = ParamDto.builder()
                .name("SECRET_KEY_BYTES")
                .build();
        final byte[]  SECRET_KEY_BYTES_CONFIG = securityConfigClient.getParam(paramDto2).getValue().getBytes();


        HOTPGenerator hotp = new HOTPGenerator.Builder(SECRET_KEY_BYTES_CONFIG)
                .withPasswordLength(OTP_LENGTH_CONFIG)
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

        //GET OTP_LENGTH from configuration service
        ParamDto paramDto1 = ParamDto.builder()
                .name("OTP_LENGTH")
                .build();
        Integer OTP_LENGTH_CONFIG = Integer.parseInt(securityConfigClient.getParam(paramDto1).getValue());

        //GET SECRET_KEY_BYTES from configuration service
        ParamDto paramDto2 = ParamDto.builder()
                .name("SECRET_KEY_BYTES")
                .build();
        final byte[]  SECRET_KEY_BYTES_CONFIG = securityConfigClient.getParam(paramDto2).getValue().getBytes();


        HOTPGenerator hotp = new HOTPGenerator.Builder(SECRET_KEY_BYTES_CONFIG)
                .withPasswordLength( OTP_LENGTH_CONFIG)
                .withAlgorithm(HMACAlgorithm.SHA256)
                .build();
        String code = hotp.generate(counter);

        return code;

    }
}
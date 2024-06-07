package com.eai.securityservice.service;

import com.bastiaanjansen.otp.HMACAlgorithm;
import com.bastiaanjansen.otp.HOTPGenerator;
import com.eai.openfeignservice.config.ConfigClient;
import com.eai.openfeignservice.config.ParamDto;
import com.eai.openfeignservice.notification.EmailSender;
import com.eai.openfeignservice.notification.NotificationClient;
import com.eai.openfeignservice.user.ClientRequest;
import com.eai.openfeignservice.user.UserClient;
import com.eai.securityservice.configuration.JwtUtil;
import com.eai.securityservice.dto.OtpEmailCompareResponse;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OtpEmailService {
    private final OtpRepository otpRepository;
    private final HistoryRepository historyRepository;
    private final Counter counter;
    private final CounterRepository counterRepository;
    private final NotificationClient notificationClient;
    private final UserClient userClient;
    private final JwtUtil jwtUtil;
    private final ConfigClient securityConfigClient;

    @Autowired
    private UserDetailsService userDetailsService;





    public String generateOtpEmail(@RequestBody OtpEmailRequest otpEmailRequest ) {

        ParamDto paramDto1 = ParamDto.builder()
                .name("NEW_DATE_GENERATION")
                .build();
        Integer NEW_DATE_GENERATION_CONFIG = Integer.parseInt(securityConfigClient.getParam(paramDto1).getValue());

        ClientRequest clientRequest = ClientRequest.builder()
                .email(otpEmailRequest.getEmail())
                .build();
        String isSent;
        boolean isClientExist = userClient.isClientExist(clientRequest);
        if (!isClientExist) {
            String generatedOtp = generateOtp(counter.getCounter());
            Otp otp = otpRepository.findByEmail(otpEmailRequest.getEmail());
            History history = historyRepository.findTopByEmailOrderByDateGenerationDesc(otpEmailRequest.getEmail());
            EmailSender emailSender = EmailSender.builder()
                    .email(otpEmailRequest.getEmail())
                    .codeOtpEmail(generatedOtp)
                    .build();
            if (otp == null) {
                history = new History(otpEmailRequest.getEmail(), counter.getCounter(), new Date());
                otp = new Otp(otpEmailRequest.getEmail(), counter.getCounter(), new Date());
                if (Objects.equals(notificationClient.sendOtpEmail(emailSender), "01")) {
                    isSent = OtpGenerationStatusEnum.SUCCESS.getLabel();
                } else {
                    isSent = OtpGenerationStatusEnum.EMAIL_ERROR.getLabel();
                }

            } else {
                 //get maxNbrGeneration from config service
                 ParamDto paramDto = ParamDto.builder()
                    .name("NBR_GENERATION")
                    .build();
                 Integer NBR_GENERATION_CONFIG = Integer.parseInt(securityConfigClient.getParam(paramDto).getValue());

                if (history.getNumGeneration() < NBR_GENERATION_CONFIG) {
                    history.setCounter(counter.getCounter());
                    history.setDateGeneration(new Date());
                    history.incrementNumGeneration();
                    otp.setCounter(counter.getCounter());
                    otp.setDateGeneration(new Date());
                    otp.setAttempts(0);
                    if (Objects.equals(notificationClient.sendOtpEmail(emailSender), "01")) {
                        isSent = OtpGenerationStatusEnum.SUCCESS.getLabel();
                    } else {
                        isSent = OtpGenerationStatusEnum.EMAIL_ERROR.getLabel();
                    }

                } else if (isPast15Minutes(history.getDateGeneration()) > NEW_DATE_GENERATION_CONFIG) {
                    history = new History(otpEmailRequest.getEmail(), counter.getCounter(), new Date());
                    otp.setCounter(counter.getCounter());
                    otp.setDateGeneration(new Date());
                    otp.setAttempts(0);
                    if(Objects.equals(notificationClient.sendOtpEmail(emailSender), "01")){
                        isSent =  OtpGenerationStatusEnum.SUCCESS.getLabel();
                    }else{
                        isSent =  OtpGenerationStatusEnum.EMAIL_ERROR.getLabel();
                    }
                } else {
                    isSent = OtpGenerationStatusEnum.MAX_GENERATED_OTP_ERROR.getLabel();
                }
            }

            otpRepository.save(otp);
            historyRepository.save(history);
            counter.incrementCounter();
            counterRepository.save(counter);
        }else {
            EmailSender emailSender = EmailSender.builder()
                    .email(otpEmailRequest.getEmail())
                    .build();
            isSent = notificationClient.sendEmailExist(emailSender);
        }
        return isSent;
    }


    public OtpEmailCompareResponse CompareOtpEmailResponse(ClientRequest otpEmailRequest) {

        Otp otp = otpRepository.findByEmail(otpEmailRequest.getEmail());
        OtpEmailCompareResponse otpEmailCompareResponse = new OtpEmailCompareResponse();

        //get DATE_EXPIRATION from config service
        ParamDto paramDto = ParamDto.builder()
                .name("DATE_EXPIRATION")
                .build();
        Integer DATE_EXPIRATION_CONFIG = Integer.parseInt(securityConfigClient.getParam(paramDto).getValue());

        if (isPast15Minutes(otp.getDateGeneration()) < DATE_EXPIRATION_CONFIG ) {

            //get maxAttemps from config service
            ParamDto paramDto1 = ParamDto.builder()
                    .name("MAX_ATTEMPTS")
                    .build();
            Integer MAX_ATTEMPTS_CONFIG = Integer.parseInt(securityConfigClient.getParam(paramDto1).getValue());

            if (otp.getAttempts() < MAX_ATTEMPTS_CONFIG) {

                Boolean isOtpValid = verifyOtp(otpEmailRequest.getUserInput(), otp.getCounter());

                Integer idClient = null;
                otp.incrementAttempt();
                otp.setIdClient(idClient);
                otpRepository.save(otp);

                if (isOtpValid) {

                    idClient = userClient.saveClient(otpEmailRequest);
                    otp.setIdClient(idClient);
                    otpRepository.save(otp);

                    UserDetails userDetails = userDetailsService.loadUserByUsername(otpEmailRequest.getEmail());
                    String newGeneratedToken = jwtUtil.generateToken(userDetails);


                    otpEmailCompareResponse.setStatusOtp(StatusOTP.VALID.getLabel());
                    otpEmailCompareResponse.setIdClient(idClient);
                    otpEmailCompareResponse.setJwtToken(newGeneratedToken);
                    return otpEmailCompareResponse;
                }else{
                    otpEmailCompareResponse.setStatusOtp(StatusOTP.INVALID.getLabel());
                    otpEmailCompareResponse.setIdClient(null);
                    otpEmailCompareResponse.setJwtToken(null);
                    return otpEmailCompareResponse;
                }
            } else{
                otpEmailCompareResponse.setStatusOtp(StatusOTP.EXPIRED_ATTEMPT.getLabel());
                otpEmailCompareResponse.setIdClient(null);
                otpEmailCompareResponse.setJwtToken(null);
                return otpEmailCompareResponse;
            }
        } else {
            otpEmailCompareResponse.setStatusOtp(StatusOTP.TIMEOUT.getLabel());
            otpEmailCompareResponse.setIdClient(null);
            otpEmailCompareResponse.setJwtToken(null);
            return otpEmailCompareResponse;
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

        HOTPGenerator hotp = new HOTPGenerator.Builder( SECRET_KEY_BYTES_CONFIG)
                .withPasswordLength( OTP_LENGTH_CONFIG)
                .withAlgorithm(HMACAlgorithm.SHA256)
                .build();
        return hotp.verify(userInput, counter);
    }


    public Boolean verifyOtp(String input, Integer counter){
        return compareOtp(input, counter);
    }


    private long isPast15Minutes(Date date) {
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

        HOTPGenerator hotp = new HOTPGenerator.Builder( SECRET_KEY_BYTES_CONFIG)
                .withPasswordLength( OTP_LENGTH_CONFIG)
                .withAlgorithm(HMACAlgorithm.SHA256)
                .build();
        String code = hotp.generate(counter);

        return code;

    }

}
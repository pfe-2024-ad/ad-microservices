package com.eai.securityservice.service;

import com.bastiaanjansen.otp.HMACAlgorithm;
import com.bastiaanjansen.otp.HOTPGenerator;
import com.eai.openfeignservice.notification.EmailSender;
import com.eai.openfeignservice.notification.NotificationClient;
import com.eai.openfeignservice.user.ClientRequest;
import com.eai.openfeignservice.user.ClientResponseForSecurity;
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

import java.time.Clock;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OtpEmailLoginService {

    private final OtpRepository otpRepository;
    private final HistoryRepository historyRepository;
    private final Counter counter;
    private final CounterRepository counterRepository;
    private final NotificationClient notificationClient;
    private final UserClient userClient;
    private final JwtUtil jwtUtil;


    @Autowired
    private UserDetailsService userDetailsService;

    private static final byte[] SECRET_KEY_BYTES = "VV3KOX7UQJ4KYAKOHMZPPH3US4CJIMH6F3ZKNB5C2OOBQ6V2KIYHM27Q".getBytes();

    public String generateOtpEmail(@RequestBody OtpEmailRequest otpEmailRequest ) {

        ClientRequest clientRequest = ClientRequest.builder()
                .email(otpEmailRequest.getEmail())
                .build();
        String isSent ;
        boolean isClientExist = userClient.isClientExist(clientRequest);

        if (!isClientExist) {
            EmailSender emailSender = EmailSender.builder()
                    .email(otpEmailRequest.getEmail())
                    .build();

            isSent = notificationClient.sendEmailRegister(emailSender);

        }else{
            String generatedOtp = generateOtp(counter.getCounter());
            Otp otp = otpRepository.findByEmail(otpEmailRequest.getEmail());
            History history = historyRepository.findTopByEmailOrderByDateGenerationDesc(otpEmailRequest.getEmail());
            EmailSender emailSender = EmailSender.builder()
                    .email(otpEmailRequest.getEmail())
                    .codeOtpEmail(generatedOtp)
                    .build();
                if (history.getNumGeneration() < 5) {
                    history.setCounter(counter.getCounter());
                    history.setDateGeneration(new Date());
                    history.incrementNumGeneration();
                    otp.setCounter(counter.getCounter());
                    otp.setDateGeneration(new Date());
                    otp.setAttempts(0);
                    if(Objects.equals(notificationClient.sendOtpEmail(emailSender), "01")){
                        isSent =  OtpGenerationStatusEnum.SUCCESS.getLabel();
                    }else{
                        isSent =  OtpGenerationStatusEnum.EMAIL_ERROR.getLabel();
                    }

                } else if (isPast30Minutes(history.getDateGeneration()) > 30) {
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
            otpRepository.save(otp);
            historyRepository.save(history);
            counter.incrementCounter();
            counterRepository.save(counter);
            System.out.println(generatedOtp);
        }
        return isSent;
    }

    public OtpEmailCompareResponse CompareOtpEmailResponse(@RequestBody ClientRequest otpEmailRequest) {

        Otp otp = otpRepository.findByEmail(otpEmailRequest.getEmail());
        OtpEmailCompareResponse otpEmailCompareResponse = new OtpEmailCompareResponse();
        if (isPast30Minutes(otp.getDateGeneration()) < 30) {
            if (otp.getAttempts() < 3) {
                Boolean isOtpValid = verifyOtp(otpEmailRequest.getUserInput(), otp.getCounter());

                Integer idClient = null;
                otp.incrementAttempt();
                otp.setIdClient(idClient);
                otpRepository.save(otp);
                if (isOtpValid) {
                    ClientRequest clientRequest = ClientRequest.builder()
                            .email(otpEmailRequest.getEmail())
                            .build();
                    System.out.println(clientRequest);
                    ClientResponseForSecurity identityClient =  userClient.getClientStep(clientRequest);
                    otp.setIdClient(identityClient.getIdClient());
                    otpRepository.save(otp);

                    UserDetails userDetails = userDetailsService.loadUserByUsername(identityClient.getIdClient().toString());
                    String newGeneratedToken = jwtUtil.generateToken(userDetails, idClient);

                    otpEmailCompareResponse.setStatusOtp(StatusOTP.VALID.getLabel());
                    otpEmailCompareResponse.setIdClient(idClient);
                    otpEmailCompareResponse.setJwtToken(newGeneratedToken);
                    otpEmailCompareResponse.setStep(identityClient.getClientStep());
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

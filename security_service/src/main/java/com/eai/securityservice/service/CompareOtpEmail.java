package com.eai.securityservice.service;

import com.bastiaanjansen.otp.HMACAlgorithm;
import com.bastiaanjansen.otp.HOTPGenerator;
import com.eai.openfeignservice.user.ClientRequest;
import com.eai.openfeignservice.user.UserClient;
import com.eai.securityservice.dto.OtpEmailRequest;
import com.eai.securityservice.model.Otp;
import com.eai.securityservice.outiles.enums.StatusOTP;
import com.eai.securityservice.repository.OtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
public class CompareOtpEmail {
    private final OtpRepository otpRepository;
    private final UserClient userClient;
    private static final byte[] SECRET_KEY_BYTES = "VV3KOX7UQJ4KYAKOHMZPPH3US4CJIMH6F3ZKNB5C2OOBQ6V2KIYHM27Q".getBytes();

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


    private long isPast30Minutes(Date date) {
        long diffInMilliseconds = new Date().getTime() - date.getTime();
        return TimeUnit.MILLISECONDS.toMinutes(diffInMilliseconds);
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
}

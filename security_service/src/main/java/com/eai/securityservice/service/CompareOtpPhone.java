package com.eai.securityservice.service;

import com.bastiaanjansen.otp.HMACAlgorithm;
import com.bastiaanjansen.otp.HOTPGenerator;
import com.eai.securityservice.dto.OtpPhoneRequest;
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
public class CompareOtpPhone {
    private final OtpRepository otpRepository;
    private static final byte[] SECRET_KEY_BYTES = "VV3KOX7UQJ4KYAKOHMZPPH3US4CJIMH6F3ZKNB5C2OOBQ6V2KIYHM27Q".getBytes();

    public StatusOTP compareOtp(@RequestBody OtpPhoneRequest otpPhoneRequest) {

        Otp otp = otpRepository.findByKeyPhoneAndNumPhone(otpPhoneRequest.getKeyPhone(), otpPhoneRequest.getNumPhone());
        if (isPast30Minutes(otp.getDateGeneration())<30) {
            if (otp.getAttempts()<3) {
                Boolean isOtpValid = verifyOtp(otpPhoneRequest.getUserInput(), otp.getCounter());
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
        boolean isvalid;
        return isvalid = compareOtp(input, counter);


    }
}

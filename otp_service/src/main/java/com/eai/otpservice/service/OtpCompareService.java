package com.eai.otpservice.service;


import com.bastiaanjansen.otp.HMACAlgorithm;
import com.bastiaanjansen.otp.TOTPGenerator;
import org.springframework.stereotype.Service;

import java.time.Duration;


@Service
public class OtpCompareService {

    public boolean compareOtp(byte[] secret, String userInput) {

        TOTPGenerator totp = new TOTPGenerator.Builder(secret)
                .withHOTPGenerator(builder -> {
                    builder.withPasswordLength(6);
                    builder.withAlgorithm(HMACAlgorithm.SHA256);
                })
                .withPeriod(Duration.ofMinutes(15))
                .build();


        return totp.verify(userInput);
    }


}

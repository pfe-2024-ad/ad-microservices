package com.eai.securityservice.service;

import com.bastiaanjansen.otp.HMACAlgorithm;
import com.bastiaanjansen.otp.HOTPGenerator;
import org.springframework.stereotype.Service;


@Service
public class OtpCompareService {
    private static final byte[] SECRET_KEY_BYTES = "VV3KOX7UQJ4KYAKOHMZPPH3US4CJIMH6F3ZKNB5C2OOBQ6V2KIYHM27Q".getBytes();


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

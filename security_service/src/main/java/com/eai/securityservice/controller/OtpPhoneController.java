package com.eai.securityservice.controller;

import com.eai.securityservice.dto.OtpPhoneRequest;
import com.eai.securityservice.outiles.enums.StatusOTP;
import com.eai.securityservice.service.CompareOtpPhone;
import com.eai.securityservice.service.GenerateOtpPhone;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/otp_phone")
@RequiredArgsConstructor
public class OtpPhoneController {
    private static final byte[] SECRET_KEY_BYTES = "VV3KOX7UQJ4KYAKOHMZPPH3US4CJIMH6F3ZKNB5C2OOBQ6V2KIYHM27Q".getBytes();
    private final CompareOtpPhone compareOtpPhone;
    private final GenerateOtpPhone generateOtpPhone;

    @PostMapping("/generate")
    public String generatePhoneOtp(@RequestBody OtpPhoneRequest otpPhoneRequest) {
        return generateOtpPhone.generateOtpPhone(otpPhoneRequest);
    }


    @PostMapping("/compare")
    public StatusOTP compareOtp(@RequestBody OtpPhoneRequest otpPhoneRequest) {
        return compareOtpPhone.compareOtp(otpPhoneRequest);

    }
}

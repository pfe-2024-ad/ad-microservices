package com.eai.securityservice.controller;
import com.eai.securityservice.service.CompareOtpEmail;
import com.eai.securityservice.service.GenerateOtpEmail;
import com.eai.securityservice.dto.OtpEmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/otp_email")
@RequiredArgsConstructor
public class OtpEmailController {
    private final CompareOtpEmail compareOtpEmail;
    private final GenerateOtpEmail generateEmailOtp;

    @PostMapping("/generate")
    public String generateEmailOtp(@RequestBody OtpEmailRequest otpEmailRequest ) {
        return generateEmailOtp.generateOtpEmail(otpEmailRequest);
    }


    @PostMapping("/compare")
    public String compareOtp(@RequestBody OtpEmailRequest otpEmailRequest) {
        return compareOtpEmail.compareOtp(otpEmailRequest);
    }
}


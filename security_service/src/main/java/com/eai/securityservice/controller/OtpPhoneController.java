package com.eai.securityservice.controller;

import com.eai.securityservice.dto.OtpPhoneRequest;
import com.eai.securityservice.outiles.enums.StatusOTP;
import com.eai.securityservice.service.OtpPhoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/otp_phone")
@RequiredArgsConstructor
public class OtpPhoneController {
    private final OtpPhoneService otpPhoneService;
    @PostMapping("/generate")
    public String generatePhoneOtp(@RequestBody OtpPhoneRequest otpPhoneRequest) {
        return otpPhoneService.generateOtpPhone(otpPhoneRequest);
    }


    @PostMapping("/compare")
    public StatusOTP compareOtp(@RequestBody OtpPhoneRequest otpPhoneRequest) {
        return otpPhoneService.compareOtp(otpPhoneRequest);

    }
}

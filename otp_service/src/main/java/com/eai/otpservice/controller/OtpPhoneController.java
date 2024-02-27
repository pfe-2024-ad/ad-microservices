package com.eai.otpservice.controller;

import com.ayman.client_otp.otp_service.dto.OtpPhoneRequest;
import com.ayman.client_otp.otp_service.service.OtpCompareService;
import com.ayman.client_otp.otp_service.service.OtpGenerateService;
import com.bastiaanjansen.otp.SecretGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/otp")
public class OtpPhoneController {

    private final OtpGenerateService otpGenerateService;
    private final OtpCompareService otpCompareService;



    @Autowired
    public OtpPhoneController(OtpGenerateService otpService,
                              OtpCompareService otpCompareService) {
        this.otpGenerateService = otpService;
        this.otpCompareService = otpCompareService;

    }

    byte[] secretKeyBytes = SecretGenerator.generate();

    @PostMapping("/generate")
    public ResponseEntity<Object> generatePhoneOtp() {
        ///
        UUID userId = UUID.randomUUID();
        String generatedOtp = otpGenerateService.generateOtp(secretKeyBytes);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("userId", secretKeyBytes);
        responseMap.put("generatedOtp", generatedOtp);

        // Return the JSON object in the response body
        return ResponseEntity.ok(responseMap);    }





    @PostMapping("/compare")
    public ResponseEntity<Boolean> compareOtp(@RequestBody OtpPhoneRequest otpPhoneRequest) {
        // Use the UUID associated with the user to verify the OTP
        boolean isOtpValid = otpCompareService.compareOtp( otpPhoneRequest.getUserId(), otpPhoneRequest.getUserInput());

        return ResponseEntity.ok(isOtpValid);
    }
}
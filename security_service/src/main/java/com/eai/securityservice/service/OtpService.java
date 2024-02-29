package com.eai.securityservice.service;

import com.eai.securityservice.dto.OtpPhoneRequest;

public interface OtpService {

    void generateOtpPhone(OtpPhoneRequest otpPhoneRequest);
}

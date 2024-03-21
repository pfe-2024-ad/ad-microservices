package com.eai.securityservice.outiles.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OtpGenerationStatusEnum {

    SUCCESS("01"),
    MAX_GENERATED_OTP_ERROR("02"),
    EMAIL_ERROR("03");

    private final String label;

}

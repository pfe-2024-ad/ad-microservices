package com.eai.otpservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtpPhoneResponse {

    private String phoneNum;
    private String code;
    private UUID userId;


}

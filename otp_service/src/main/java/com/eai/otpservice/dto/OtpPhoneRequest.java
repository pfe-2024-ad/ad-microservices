package com.eai.otpservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtpPhoneRequest {

    private String phoneNumber;
    private String userInput;
    private byte[] userId;



    // Constructors, getters, setters
}

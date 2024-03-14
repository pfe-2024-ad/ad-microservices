package com.eai.securityservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtpPhoneRequest {

    private String keyPhone;
    private String numPhone;
    private String userInput;
    private Integer idClient;



    // Constructors, getters, setters
}

package com.eai.securityservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class OtpEmailCompareResponse {
    private String statusOtp;
    private Integer idClient;
}

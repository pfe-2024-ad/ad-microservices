package com.eai.dto.mocks;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CmiOutputDTO {
    private Integer idClient;
    private Integer transId;
    private String timeStamp;
    private String redirectUrl;
    private Double amount;
    private String paymentNotice;
    private String hash;
    private String errCode;
    private String procReturnCode;
    private String oid;
    private String callbackResponseApproved; // **
    private String callbackResponseFailed; // **
}
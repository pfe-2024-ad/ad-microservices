package com.eai.payment_service.dto;

import com.eai.payment_service.outils.enums.CmiLanguage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CmiHashCalculationInput {
    private String amount;
    private String paymentNotice;
    private Integer clientId;
    private String oid;
    private CmiLanguage lang;
    private String email;
    private String billToName;
    private String rnd;
    private String tel;
    private String billToStreet;
    private String billToCity;
    private String billToPostalCode;
    private String billToCountry;
    private String callbackUrl;
    private String okUrl;
    private String failUrl;
    private String shopUrl;
    private String cmiUrl;
    private String hashAlgorithm;
}

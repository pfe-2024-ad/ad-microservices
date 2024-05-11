package com.eai.dto.mocks;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CmiInputDTO {
    private Integer idClient;
    private Double amount;
    private String oid;
    private String lang;
    private String email;
    private String billToName;
    private String rnd;
    private String billToAdresse;
    private String billToCity;
    private Integer billToPostalCode;
    private String billToCountry;
    private String tel;
    private String currency;
    private String storeType;
    private String transType;
    private String hashAlgo;
    private String encoding;
    private Boolean callBackResponse;
    private String okUrl;
    private String failUrl;
    private String callBack;
    private String shopUrl;
    private String cmiUrl;
    private String hash;
}


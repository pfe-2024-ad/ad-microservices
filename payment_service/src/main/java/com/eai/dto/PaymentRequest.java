package com.eai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest {
    private Integer idClient;
    private Double amount;
    private Date date_versement;
}
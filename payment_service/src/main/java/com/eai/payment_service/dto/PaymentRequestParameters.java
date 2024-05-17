package com.eai.payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestParameters {


    private CmiHashCalculationInput cmiHashCalculationInput;
    private String hash;


}

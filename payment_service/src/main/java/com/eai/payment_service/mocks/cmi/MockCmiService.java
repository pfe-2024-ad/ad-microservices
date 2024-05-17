package com.eai.payment_service.mocks.cmi;

import com.eai.payment_service.dto.PaymentRequestParameters;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
public class MockCmiService {

    public MultiValueMap<String,String> mockCmi(PaymentRequestParameters paymentRequestParameters) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        map.add("amount", paymentRequestParameters.getCmiHashCalculationInput().getAmount());
        map.add("paymentNotice",paymentRequestParameters.getCmiHashCalculationInput().getPaymentNotice());
        map.add("clientId", String.valueOf(paymentRequestParameters.getCmiHashCalculationInput().getClientId()));
        map.add("oid", paymentRequestParameters.getCmiHashCalculationInput().getOid());
        map.add("lang", String.valueOf(paymentRequestParameters.getCmiHashCalculationInput().getLang()));
        map.add("email", paymentRequestParameters.getCmiHashCalculationInput().getEmail());
        map.add("billToName", paymentRequestParameters.getCmiHashCalculationInput().getBillToName());
        map.add("rnd", paymentRequestParameters.getCmiHashCalculationInput().getRnd());
        map.add("tel", paymentRequestParameters.getCmiHashCalculationInput().getTel());
        map.add("billToStreet", paymentRequestParameters.getCmiHashCalculationInput().getBillToStreet());
        map.add("billToCity", paymentRequestParameters.getCmiHashCalculationInput().getBillToCity());
        map.add("billToPostalCode", paymentRequestParameters.getCmiHashCalculationInput().getBillToPostalCode());
        map.add("billToCountry", paymentRequestParameters.getCmiHashCalculationInput().getBillToCountry());
        map.add("callbackUrl", paymentRequestParameters.getCmiHashCalculationInput().getCallbackUrl());
        map.add("okUrl", paymentRequestParameters.getCmiHashCalculationInput().getOkUrl());
        map.add("failUrl", paymentRequestParameters.getCmiHashCalculationInput().getFailUrl());
        map.add("shopUrl", paymentRequestParameters.getCmiHashCalculationInput().getShopUrl());
        map.add("cmiUrl", paymentRequestParameters.getCmiHashCalculationInput().getCmiUrl());
        map.add("hash", paymentRequestParameters.getHash());
        map.add("ProcReturnCode", "00");
        map.add("transId", "xxxx");
        map.add("hashAlgorithm", "ver3");
        map.add("encoding", "xxx");

        return map;
    }
}

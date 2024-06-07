package com.eai.payment_service.controller;

import com.eai.payment_service.dto.NewPaymentRequest;
import com.eai.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;


    @PostMapping("/payment/new")
    public  String  NewPayment(@RequestBody NewPaymentRequest newPaymentRequest) throws Exception {

        return paymentService.NewPayment(newPaymentRequest.getIdClient(), newPaymentRequest.getAmount());

    }

    @PostMapping("/cmi/ok")
    public String ok(@RequestBody LinkedMultiValueMap<String, String> parameters) {
        return paymentService.ok(parameters);

    }
}

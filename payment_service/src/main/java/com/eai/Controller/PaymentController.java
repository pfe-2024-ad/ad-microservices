package com.eai.Controller;

import com.eai.Service.PaymentService;
import com.eai.dto.PaymentRequest;
import com.eai.dto.mocks.CmiOutputDTO;
import com.eai.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("ok-payment")
    public String okPayment(@RequestBody CmiOutputDTO paymentResponseDTO) {
        return paymentService.okPayment(paymentResponseDTO);
    }
    @PostMapping("fail-payment")
    public String failPayment (@RequestBody CmiOutputDTO paymentResponseDTO) {
        return paymentService.failPayment(paymentResponseDTO);
    }
    @PostMapping("cancel-payment")
    public String cancelPayment(@RequestBody CmiOutputDTO cmiResponseDTO) {
        return paymentService.cancelPayment(cmiResponseDTO, cmiResponseDTO.getOid());
    }
    @PostMapping("create-payment")
    public String createPayment(@RequestBody PaymentRequest paymentRequest) {
        return paymentService.createPayment(paymentRequest);
    }
}

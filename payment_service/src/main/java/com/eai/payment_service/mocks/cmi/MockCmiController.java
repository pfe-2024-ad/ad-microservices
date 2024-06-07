package com.eai.payment_service.mocks.cmi;

import com.eai.payment_service.dto.PaymentRequestParameters;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("cmi/mock")
@Slf4j
public class MockCmiController {

    private final MockCmiService mockCmiService;

    @PostMapping
    public MultiValueMap<String,String> mockCmi(@RequestBody PaymentRequestParameters paymentRequestParameters) {
        return mockCmiService.mockCmi(paymentRequestParameters);
    }
}

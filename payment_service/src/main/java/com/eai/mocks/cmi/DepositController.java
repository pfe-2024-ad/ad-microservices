package com.eai.mocks.cmi;

import com.eai.dto.mocks.CmiInputDTO;
import com.eai.dto.mocks.CmiOutputDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("mocks/cmi/deposit")
public class DepositController {

    private final DepositService depositService;

    @PostMapping
    public ResponseEntity<CmiOutputDTO> PaymentResponse(@RequestBody CmiInputDTO paymentRequestDTO) {
        CmiOutputDTO cmiOutputDTO = depositService.mockedPaymentResponse(paymentRequestDTO);
        return new ResponseEntity<>(cmiOutputDTO, HttpStatus.OK);
    }
}

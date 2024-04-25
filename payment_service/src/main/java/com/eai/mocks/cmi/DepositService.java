package com.eai.mocks.cmi;

import com.eai.dto.mocks.CmiInputDTO;
import com.eai.dto.mocks.CmiOutputDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DepositService {

    public CmiOutputDTO mockedPaymentResponse (CmiInputDTO paymentRequestDTO) {

        CmiOutputDTO paymentResponseDTO = new CmiOutputDTO();
        paymentResponseDTO.setIdClient(61);
        paymentResponseDTO.setTransId(22);
        paymentResponseDTO.setTimeStamp("yyyy.MM.dd.HH.mm.ss");
        paymentResponseDTO.setRedirectUrl("mockedRedirectUrl.com");
        paymentResponseDTO.setErrCode("mockedErrCode");
        paymentResponseDTO.setAmount(1000.00);
        paymentResponseDTO.setPaymentNotice("mockedPaymentNotice");
        paymentResponseDTO.setHash("mockedHash");
        paymentResponseDTO.setProcReturnCode("mockedProcReturnCode");
        paymentResponseDTO.setOid("mockedOid");
        paymentResponseDTO.setCallbackResponseApproved("mockedResponseCallbackApproved");
        paymentResponseDTO.setCallbackResponseFailed("mockedResponseCallbackFailed");

        return paymentResponseDTO;

    }
}

package com.eai.Service;
import com.eai.dto.mocks.CmiInputDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
@Slf4j
public class CmiService {


    public CmiInputDTO buildPaymentRequestDTO (){

        CmiInputDTO paymentRequestDTO = new CmiInputDTO();
        paymentRequestDTO.setIdClient(3);
        paymentRequestDTO.setAmount(2000.00);
        paymentRequestDTO.setOid("1.00.98.90.8888.920.01");
        paymentRequestDTO.setLang("fr");
        paymentRequestDTO.setEmail("fadwa@gmail.com");
        paymentRequestDTO.setRnd("Enf6");
        paymentRequestDTO.setBillToAdresse("Inara");
        paymentRequestDTO.setBillToCity("casa");
        paymentRequestDTO.setBillToPostalCode(9090);
        paymentRequestDTO.setBillToCountry("maroc");
        paymentRequestDTO.setCurrency("MAD");
        paymentRequestDTO.setStoreType("mockedStoreType");
        paymentRequestDTO.setTransType("mockedTransType");
        paymentRequestDTO.setHashAlgo("mockedHashAlgo");
        paymentRequestDTO.setEncoding("UTF-8");
        paymentRequestDTO.setCallBackResponse(true);
        paymentRequestDTO.setOkUrl("mockedOkUrl.com");
        paymentRequestDTO.setFailUrl("mockedFailUrl.com");
        paymentRequestDTO.setCallBack("mockedCallbackUrl.com");
        paymentRequestDTO.setShopUrl("mockedCancelUrl.com");
        paymentRequestDTO.setHash("mockedHash");

        return paymentRequestDTO;

    }


}
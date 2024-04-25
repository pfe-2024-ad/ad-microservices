package com.eai.Service;
import com.eai.dto.PaymentRequest;
import com.eai.dto.mocks.CmiInputDTO;
import com.eai.dto.mocks.CmiOutputDTO;
import com.eai.enums.Payment_Status;
import com.eai.model.Payment;
import com.eai.openfeignservice.user.ClientResponseForPayment;
import com.eai.openfeignservice.user.UserClient;
import com.eai.repositories.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserClient clientServiceFeignClient;
    private final CmiService cmiService;
    private static final Double min_amount = 100.00;
    private static final Double max_amount = 5000.00;
    public String createPayment(PaymentRequest paymentRequest) {

        if (paymentRequest.getAmount() < min_amount ||
                paymentRequest.getAmount() > max_amount) {
            return "Amount should be between" + min_amount + "and" + max_amount;

        }
        Integer idClient = paymentRequest.getIdClient();

        Optional<ClientResponseForPayment> clientResponseForPayment = Optional.ofNullable(clientServiceFeignClient.getClientForPayment(idClient));

        if (!clientResponseForPayment.isPresent()) {
            return "client not found";
        }
        try {
            RestTemplate restTemplate = new RestTemplate();
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            String cmiServiceUrl = "http://localhost:7777/agd/payment-service/mocks/cmi";


            CmiInputDTO paymentRequestDTO = cmiService.buildPaymentRequestDTO();
            HttpEntity<CmiInputDTO> requestEntity = new HttpEntity<>(paymentRequestDTO, headers);
            ResponseEntity<CmiOutputDTO> responseEntity = restTemplate.postForEntity(cmiServiceUrl,requestEntity , CmiOutputDTO.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {

                return "Payment request sent successfully";
            } else {
                return "Failed to send payment request. Response code: " + responseEntity.getStatusCodeValue();
            }
        }catch (Exception e){
            return "Error creating payment: " + e.getMessage();
        }


    }



    public Boolean isAnyMandatoryFieldMissing(CmiOutputDTO paymentResponseDTO) {
        if (paymentResponseDTO.getTransId() == null ||
                paymentResponseDTO.getAmount() == null ||
                paymentResponseDTO.getOid() == null ) {
            return true;
        }
        return false;
    }
    public Boolean isValidHash(CmiOutputDTO paymentResponseDTO){
        CmiInputDTO paymentRequestDTO = new CmiInputDTO();
        if(paymentResponseDTO.getHash().equals(paymentRequestDTO.getHash()) ) {
            return true;
        }
        return false;
    }

    public String validateCmiResponse (CmiOutputDTO paymentResponseDTO) {
        if(!isValidHash(paymentResponseDTO)){
            log.error("Hash mismatch in CMI response");
            return  paymentResponseDTO.getCallbackResponseFailed();
        }

        if (paymentResponseDTO.getErrCode()!= null) {
            log.error("Found in CMI response: ErrCode='{}'", paymentResponseDTO.getErrCode());
            return  paymentResponseDTO.getCallbackResponseFailed();
        }
        if (!"00".equals(paymentResponseDTO.getProcReturnCode())) {
            log.error("Found in CMI response: ProcReturnCode='{}'", paymentResponseDTO.getProcReturnCode());
            return  paymentResponseDTO.getCallbackResponseApproved();
        }
        if (isAnyMandatoryFieldMissing(paymentResponseDTO)) {
            log.error("Missing in CMI response: '{}'");
            return  paymentResponseDTO.getCallbackResponseFailed();
        }
        return paymentResponseDTO.getCallbackResponseApproved();
    }


    public String okPayment(CmiOutputDTO paymentResponseDTO) {
        String validation = validateCmiResponse(paymentResponseDTO);
        if(StringUtils.isNotEmpty(validation)) {
            return "Payment validation successful";
        }
        //payment passed successfully
        try {
            Payment payment = Payment.builder()
                    .idClient(paymentResponseDTO.getIdClient())
                    .amount(paymentResponseDTO.getAmount())
                    .date_versement(new Date())
                    .build();
            paymentRepository.save(payment);

            Optional<ClientResponseForPayment> clientResponseForPayment = Optional.ofNullable(clientServiceFeignClient.getClientForPayment(paymentResponseDTO.getIdClient()));
            if(clientResponseForPayment.isPresent()) {
                ClientResponseForPayment client = clientResponseForPayment.get();
            }else {
                return "Failed to retrieve client information";
            }
            return Payment_Status.SUCCESS.getLabel();
        }catch (Exception e) {
            log.error("Error processing payment", e);

        }
        return Payment_Status.SUCCESS.getLabel();
    }
    public String failPayment (CmiOutputDTO paymentResponseDTO) {

        try {
            Payment payment = Payment.builder()
                    .idClient(paymentResponseDTO.getIdClient())
                    .amount(paymentResponseDTO.getAmount())
                    .date_versement(new Date())
                    .build();
            paymentRepository.save(payment);

            Optional<ClientResponseForPayment> clientResponseForPayment = Optional.ofNullable(clientServiceFeignClient.getClientForPayment(paymentResponseDTO.getIdClient()));
            if(clientResponseForPayment.isPresent()) {
                ClientResponseForPayment client = clientResponseForPayment.get();
            }else {
                log.error("Failed to retrieve client information for payment ID: {}", paymentResponseDTO.getIdClient());

            }
            return Payment_Status.FAILED.getLabel();
        }catch (Exception e) {
            log.error("Error processing failed payment", e);

        }
        return  Payment_Status.FAILED.getLabel();

    }

    public String cancelPayment(CmiOutputDTO cmiResponseDTO, String oid) {
        try {
            log.info("Payment oid '{}' (Url-encoded) was canceled",
                    URLEncoder.encode(cmiResponseDTO.getOid(), StandardCharsets.UTF_8.name()));

            Optional<Payment> payment = paymentRepository.findByOid(oid);
            if(!payment.isPresent()) {
                log.error("No payment found for oid: {}", oid);
                Payment_Status.FAILED.getLabel();
            }
            Optional<ClientResponseForPayment> clientResponseForPayment = Optional.ofNullable(clientServiceFeignClient.getClientForPayment(cmiResponseDTO.getIdClient()));
            if(clientResponseForPayment.isPresent()) {
                ClientResponseForPayment client = clientResponseForPayment.get();
            }else {
                log.error("No client found for payment with oid: {}", oid);

            }
            return Payment_Status.CANCEL.getLabel();

        }catch (UnsupportedEncodingException e){
            log.error("Error encoding OID: {}", oid, e);

        }
        return Payment_Status.CANCEL.getLabel();
    }


}





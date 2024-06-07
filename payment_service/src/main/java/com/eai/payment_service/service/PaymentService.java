package com.eai.payment_service.service;

import com.eai.openfeignservice.user.ClientRequest;
import com.eai.openfeignservice.user.ClientResponseForPayment;
import com.eai.openfeignservice.user.UserClient;
import com.eai.openfeignservice.user.outils.enums.ClientStep;
import com.eai.payment_service.dto.CmiHashCalculationInput;
import com.eai.payment_service.dto.CmiRedirections;
import com.eai.payment_service.dto.CmiResponse;
import com.eai.payment_service.dto.PaymentRequestParameters;
import com.eai.payment_service.model.Payment;
import com.eai.payment_service.outils.enums.CmiLanguage;
import com.eai.payment_service.outils.enums.PaymentStatus;
import com.eai.payment_service.repository.PaymentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final UserClient userClient;
    private final PaymentRepository paymentRepository;


    private static final int MINIMUM_AMOUNT = 100;
    private static final int MAXIMUM_AMOUNT = 5000;
    private static final String PAYMENT_NOTICE = "payment notice";
    private static final String HASH_ALGORITHM = "ver3";
    private static final String CMI_URL = "" ;
    public static final String CMI_FIELD_OID = "oid";
    private static final String CMI_FIELD_AMOUNT = "amount";
    private static final String CMI_FIELD_PAYMENTNOTICE = "paymentNotice";
    private static final String CMI_FIELD_ERRCODE = "ErrCode";
    private static final String CMI_FIELD_PROCRETURNCODE = "ProcReturnCode";
    private static final String CMI_FIELD_TRANSID = "transId";
    private static final String CMI_CALLBACK_RESPONSE_FAILURE = "FAILURE";
    private static final String CMI_CALLBACK_RESPONSE_APPROVED = "APPROVED";

    public  String  NewPayment(Integer idClient, Float amount) throws Exception {

        if (amount < MINIMUM_AMOUNT || amount > MAXIMUM_AMOUNT) {
            return PaymentStatus.AMOUNT_NOT_ACCEPTED.getLabel();
        }
        if(paymentRepository.findPaymentByIdClient(idClient) != null){
            return PaymentStatus.PYMENT_ALREADY_MADE.getLabel();
        }

        ClientResponseForPayment client = userClient.getClientForPayment(idClient);
        log.info(client.toString());
        CmiRedirections cmiRedirections = getCmiRedirections();
        PaymentRequestParameters paymentRequestParameters = createPaymentRequestParameters(
                client,
                amount,
                CmiLanguage.Fr,
                PAYMENT_NOTICE,
                CMI_URL,
                cmiRedirections);

        // Créer une instance de RestTemplate et les en-têtes de la requête
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        HttpEntity<PaymentRequestParameters> requestEntityId = new HttpEntity<>(paymentRequestParameters, headers);

        // Effectuez la requête POST avec RestTemplate
        MultiValueMap<String, String> response = restTemplate.postForObject("http://localhost:7777/agd/payment-service/cmi/mock", requestEntityId, MultiValueMap.class);


        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(response, headers);

        // Effectuez la requête POST avec RestTemplate
        String response2 = restTemplate.postForObject("http://localhost:7777/agd/payment-service/cmi/ok", requestEntity, String.class);

        return response2;
    }

    public void annulerPayment(Integer idClient){
        ClientRequest clientRequest = ClientRequest.builder()
                .idClient(idClient)
                .step(ClientStep.PAYMENT_STEP)
                .build();
        userClient.setClientStep(clientRequest);
    }

    @NotNull
    private CmiRedirections getCmiRedirections() {
        return CmiRedirections
                .builder()
                .callbackUrl("http://localhost:3000/payment/callback")
                .okUrl("http://localhost:3000/payment/ok")
                .failUrl("http://localhost:3000/payment/fail")
                .shopUrl("http://localhost:3000/payment/cancel")
                .build();
    }

    @NotNull
    public PaymentRequestParameters createPaymentRequestParameters(ClientResponseForPayment client, float amount, CmiLanguage language, String paymentNotice, String cmiUrl, CmiRedirections cmiRedirections) throws Exception {
        CmiHashCalculationInput cmiHashCalculationInput = buildCmiHashCalculationInput(client, amount, language, paymentNotice, cmiUrl, cmiRedirections);


        log.info("cmiHashCalculationInput Map");
        log.info(toMap(cmiHashCalculationInput).toString());

        String storeKey = "aaa";
        SortedMap<String, String> sortedCaseInsensitiveProperties = convertToCaseInsensitiveSortedMap(toMap(cmiHashCalculationInput));

        String hash = computeHash(sortedCaseInsensitiveProperties, storeKey);

        return new PaymentRequestParameters(cmiHashCalculationInput, hash);
    }

    private CmiHashCalculationInput buildCmiHashCalculationInput(ClientResponseForPayment client, float amount, CmiLanguage lang, String paymentNotice, String cmiUrl, CmiRedirections cmiRedirections) {
        String fullName = client.getNom() + " " + client.getPrenom();
        String oid = getOrderId(client);

        return CmiHashCalculationInput
                .builder()
                .amount(Float.toString(amount))
                .paymentNotice(paymentNotice)
                .clientId(client.getIdClient())
                .oid(oid)
                .lang(lang)
                .email(asAlphaNumeric(removeTrailingNewLines(client.getEmail()), 64))
                .billToName(asAlphaNumeric(removeTrailingNewLines(fullName), 255))
                .rnd(getRandomAlphaNumeric(20))
                .tel(asAlphaNumeric(client.getNumTel(), 32))
                .billToStreet(asAlphaNumeric(removeTrailingNewLines(client.getAdresseResidence()), 255))
                .billToCity(asAlphaNumeric(removeTrailingNewLines(client.getVille()), 64))
                .billToPostalCode(asAlphaNumeric(removeTrailingNewLines(client.getCodePostal()), 32))
                .billToCountry(removeTrailingNewLines(client.getCountry()))
                .callbackUrl(cmiRedirections.getCallbackUrl())
                .okUrl(cmiRedirections.getOkUrl())
                .failUrl(cmiRedirections.getFailUrl())
                .shopUrl(cmiRedirections.getShopUrl())
                .cmiUrl(CMI_URL)
                .hashAlgorithm(HASH_ALGORITHM)
                .build();
    }

    @NotNull
    private String getOrderId(ClientResponseForPayment client) {
        String creationDateAsHex = Hex.encodeHexString(client.getDateCreation().getBytes(StandardCharsets.UTF_8));
        return client.getIdClient().toString() + "-" + creationDateAsHex;
    }

    public static String removeTrailingNewLines(String text) {

        return trimEnd(text, '\n');
    }

    private static String trimEnd(String text, char c) {
        if (text.lastIndexOf(c) < 0) {
            return text;
        }
        String escapedCharacter = Pattern.quote(Character.toString(c));
        return text.replaceAll(escapedCharacter + "*$", "");
    }

    private static String asAlphaNumeric(String value, int maxLength) {
        if (value == null) {
            return null;
        }
        return value
                .substring(0, Math.min(maxLength, value.length()))
                .replaceAll("[\n\r]", " "); // Handling of \n in hash calculation is not documented
    }


    //Cette méthode convertToCaseInsensitiveSortedMap prend une Map en entrée et retourne
    //une SortedMap contenant les mêmes éléments, mais avec les clés triées de manière insensible
    //à la casse.
    @NotNull
    private static SortedMap<String, String> convertToCaseInsensitiveSortedMap(Map map) {
        SortedMap<String, String> sortedCaseInsensitiveMap = new TreeMap<>(String::compareToIgnoreCase);
        for (Object entryObject : map.entrySet()) {
            Map.Entry entry = (Map.Entry) entryObject;
            String key = entry.getKey().toString();
            String value = entry.getValue() != null ? entry.getValue().toString() : null;
            sortedCaseInsensitiveMap.put(key, value);
        }
        if (sortedCaseInsensitiveMap.size() < map.size()) {
            throw new IllegalArgumentException("Some properties differ only by case");
        }
        return sortedCaseInsensitiveMap;
    }

    private static String getRandomAlphaNumeric(int maxLength) {
        if (maxLength < 1) {
            throw new IllegalArgumentException("Invalid value for maxLength: " + maxLength);
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < maxLength) {
            sb.append(UUID.randomUUID().toString().replace("-", ""));
        }
        sb.setLength(maxLength);
        return sb.toString();
    }

    private static Map toMap(Object objectToHash) {
        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.convertValue(objectToHash, Map.class);
        return map;
    }

    ///
    public String ok(LinkedMultiValueMap<String, String> parameters) {

        ClientResponseForPayment client = null;
        try {
            CmiResponse cmiResponse = CmiResponse.from(parameters);
            log.info("cmiResponse : ");
            log.info(cmiResponse.getData().toString());

            Optional<ResponseEntity<String>> errorResponse = validateCmiResponse(cmiResponse);

            if (errorResponse.isPresent()) {
                log.info("error is present");
                return PaymentStatus.ERROR.getLabel();
            }

            client = getClient(cmiResponse);
            log.info("Payment /ok");

            String amount = cmiResponse.getData().get(CMI_FIELD_AMOUNT);
            String transId = cmiResponse.getData().get(CMI_FIELD_TRANSID);
            addPayment(client.getIdClient(), amount, transId);

            ClientRequest clientRequest = ClientRequest.builder()
                    .idClient(client.getIdClient())
                    .step(ClientStep.PAYMENT_STEP)
                    .build();
            userClient.setClientStep(clientRequest);

        } catch (Exception e) {
            log.error("Error in OK endpoint", e);
        }

        return PaymentStatus.SUCCESS.getLabel();
    }



    @NotNull
    private ClientResponseForPayment getClient (@NotNull CmiResponse cmiResponse) throws Exception {
        String oid = cmiResponse.getData().get(CMI_FIELD_OID);
        if (oid == null) {
            log.error("Get form by CMI oid is not possible because oid is null");
        }

        return getClientByOid(oid);
    }

    @NotNull
    private ClientResponseForPayment getClientByOid (@NotNull String oid) throws Exception {
        String[] splitOid = oid.split("-");
        if (splitOid.length != 2) {
            throw new Exception("Invalid oid format: " + oid);
        }
        String formId = splitOid[0];
        ClientResponseForPayment client = userClient.getClientForPayment(Integer.parseInt(formId));

        return client;
    }

    public boolean isValid (CmiResponse cmiResponse) throws Exception {

        if (!cmiResponse.getData().containsKey("hash")) {
            log.error("CMI response doesn't contain a hash");
            return false;
        }
        String storeKey = "bbb";
        log.info("CMI storeKey (encrypted): {}", Base64.getEncoder().encodeToString(storeKey.getBytes()));
        String computedHash = computeHash(cmiResponse.getData(), storeKey);

        log.info("resultat boolean isValid");
        log.info(String.valueOf(computedHash.equalsIgnoreCase(cmiResponse.getData().get("hash"))));
        return computedHash.equalsIgnoreCase(cmiResponse.getData().get("hash"));
    }

    public Optional<ResponseEntity<String>> validateCmiResponse (CmiResponse cmiResponse) throws Exception {
        if (!isValid(cmiResponse)) {
            log.error("Hash mismatch in CMI response");
            return Optional.of(response(HttpStatus.OK, CMI_CALLBACK_RESPONSE_FAILURE));
        }
        if (cmiResponse.getData().containsKey(CMI_FIELD_ERRCODE)) {
            log.error("Found in CMI response: ErrCode='{}'", cmiResponse.getData().get(CMI_FIELD_ERRCODE));
            return Optional.of(response(HttpStatus.OK, CMI_CALLBACK_RESPONSE_FAILURE));
        }
        if (!"00".equals(cmiResponse.getData().get(CMI_FIELD_PROCRETURNCODE))) {
            log.error("Found in CMI response: ProcReturnCode='{}'", cmiResponse.getData().get(CMI_FIELD_PROCRETURNCODE));
            return Optional.of(response(HttpStatus.OK, CMI_CALLBACK_RESPONSE_APPROVED));
        }

        ImmutableList<String> mandatoryCmiResponseFields = ImmutableList.of(CMI_FIELD_OID, CMI_FIELD_AMOUNT,
                CMI_FIELD_PAYMENTNOTICE, CMI_FIELD_TRANSID);
        for (String field : mandatoryCmiResponseFields) {
            if (!cmiResponse.getData().containsKey(field)) {
                log.error("Missing in CMI response: '{}'", field);
                return Optional.of(response(HttpStatus.OK, CMI_CALLBACK_RESPONSE_FAILURE));
            }
        }
        return Optional.empty();
    }
    @NotNull
    private ResponseEntity<String> response (HttpStatus httpStatus, String body){
        return ResponseEntity.status(httpStatus).body(body);
    }

    public String computeHash (SortedMap < String, String > sortedCaseInsensitiveProperties, String storeKey) throws
            NoSuchAlgorithmException {

        if (!sortedCaseInsensitiveProperties.containsKey("hashAlgorithm")
                || !"ver3".equalsIgnoreCase(sortedCaseInsensitiveProperties.get("hashAlgorithm"))) {
            throw new IllegalArgumentException("Unknown hashAlgorithm: " + sortedCaseInsensitiveProperties.get("hashAlgorithm"));
        }
        Predicate<Map.Entry<String, String>> isPropertyToHash = e ->
                !"hash".equalsIgnoreCase(e.getKey())
                        && !"encoding".equalsIgnoreCase(e.getKey());

        Stream<String> valuesToHash = sortedCaseInsensitiveProperties
                .entrySet()
                .stream()
                .filter(isPropertyToHash)
                .map(Map.Entry::getValue);
        String stringToHash =
                Stream.concat(valuesToHash, Stream.of(storeKey))
                        .map(PaymentService::formatValueForHash)
                        .collect(Collectors.joining("|"));
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        messageDigest.update(stringToHash.getBytes(StandardCharsets.UTF_8));
        log.info("result computeHash");
        log.info(new String(org.apache.commons.codec.binary.Base64.encodeBase64(messageDigest.digest()), StandardCharsets.UTF_8));
        return new String(org.apache.commons.codec.binary.Base64.encodeBase64(messageDigest.digest()), StandardCharsets.UTF_8);
    }

    private static String formatValueForHash (Object value){
        if (value == null) {
            return "";
        }
        String valueString = value.toString();
        if (valueString == null) {
            return "";
        }
        return valueString
                .replaceAll("document.", "document.")
                .replace("\\", "\\\\")
                .replace("|", "\\|");
    }

    private void addPayment(Integer idClient, String amount, String transId){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Payment payment = Payment.builder()
                .idClient(idClient)
                .amount(amount)
                .date(dateFormat.format(new Date()))
                .transId(transId)
                .build();
        paymentRepository.save(payment);
    }
}

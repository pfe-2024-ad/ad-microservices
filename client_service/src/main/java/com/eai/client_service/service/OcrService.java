package com.eai.client_service.service;

import com.eai.client_service.dto.mocks.ocr.ClientResponseDto;
import com.eai.client_service.dto.InfoClientRequest;
import com.eai.client_service.dto.mocks.ocr.trustface.FluxEntreeTrustFaceDto;
import com.eai.client_service.dto.mocks.ocr.trustface.FluxSortieTrustFaceDto;
import com.eai.client_service.dto.mocks.ocr.trustid.FluxEntreeTrustIdDto;
import com.eai.client_service.dto.mocks.ocr.trustid.FluxSortieTrustIdDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OcrService {

    private final ClientService clientService;
    public ClientResponseDto getInfosCIN(MultipartFile[] files) {
        String base64_RECTO = "";
        String base64_VERSO = "";
        String base64_SELFIE = "";

        for (int i = 0; i < files.length; i++) {
            try {
                log.info("Processing image " + (i + 1));
                byte[] bytes = files[i].getBytes();
                String encodedFile = Base64.encodeBase64String(bytes);
                switch (i) {
                    case 0:
                        base64_RECTO = encodedFile;
                        break;
                    case 1:
                        base64_VERSO = encodedFile;
                        break;
                    case 2:
                        base64_SELFIE = encodedFile;
                        break;
                    default:
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Créer une instance de RestTemplate et les en-têtes de la requête
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        FluxEntreeTrustIdDto fluxEntreeTrustIdDto = buildFluxEntreeTrustIdDto(base64_RECTO, base64_VERSO);
        HttpEntity<FluxEntreeTrustIdDto> requestEntityId = new HttpEntity<>(fluxEntreeTrustIdDto, headers);
        ResponseEntity<FluxSortieTrustIdDto> responseEntityId = restTemplate.postForEntity("http://localhost:7777/agd/client-service/mocks/ocr/trustid", requestEntityId, FluxSortieTrustIdDto.class);
        //Nous utilisons la méthode postForEntity de RestTemplate pour envoyer la requête HTTP POST avec l'URL cible, l'objet de la requête et le type de réponse attendu.
        //log.info(responseEntityId.getBody().getDocumentDto().getNom());

        FluxEntreeTrustFaceDto fluxEntreeTrustFaceDto = buildFluxEntreeTrustFaceDto(
                responseEntityId.getBody().getDocumentDto().getImagePortrait(), base64_SELFIE);
        HttpEntity<FluxEntreeTrustFaceDto> requestEntityFace = new HttpEntity<>(fluxEntreeTrustFaceDto, headers);
        ResponseEntity<FluxSortieTrustFaceDto> responseEntityFace = restTemplate.postForEntity("http://localhost:7777/agd/client-service/mocks/ocr/trustface", requestEntityFace, FluxSortieTrustFaceDto.class);


        //log.info(responseEntityFace.getBody().getResult().getResults().getFirst());

        InfoClientRequest infoClientRequest = InfoClientRequest.builder()
                .idClient(5)
                .nom(responseEntityId.getBody().getDocumentDto().getNom())
                .prenom(responseEntityId.getBody().getDocumentDto().getPrenom())
                .dateNaissance(responseEntityId.getBody().getDocumentDto().getDateNaissance())
                .cin(responseEntityId.getBody().getDocumentDto().getNumeroPersonel())
                .adresseResidence(responseEntityId.getBody().getDocumentDto().getAddress())
                .build();
        clientService.updateInfoClient(infoClientRequest);

        return clientService.getClient(infoClientRequest.getIdClient());

    }

    private FluxEntreeTrustIdDto buildFluxEntreeTrustIdDto(String base64_recto, String base64_verso) {
        FluxEntreeTrustIdDto fluxEntreeTrustIdDto = new FluxEntreeTrustIdDto();

        // Mocking data for FluxEntreeTrustIdDto
        fluxEntreeTrustIdDto.setCodeClient("12345"); // Setting codeClient

        // Adding Documents to the list
        List<FluxEntreeTrustIdDto.Document> documents = new ArrayList<>();


        // First document
        FluxEntreeTrustIdDto.Document document1 = new FluxEntreeTrustIdDto.Document();
        document1.setBase64DocumentString(base64_recto);
        document1.setPageIndex(1);
        document1.setFileFormat("png");
        document1.setLightingMode("");
        document1.setReferenceDoc("");
        documents.add(document1);

        // Second document
        FluxEntreeTrustIdDto.Document document2 = new FluxEntreeTrustIdDto.Document();
        document2.setBase64DocumentString(base64_verso);
        document2.setPageIndex(2);
        document2.setFileFormat("jpg");
        document2.setLightingMode("");
        document2.setReferenceDoc("");
        documents.add(document2);

        // Setting documents list
        fluxEntreeTrustIdDto.setDocument(documents);

        return fluxEntreeTrustIdDto;

    }

    private FluxEntreeTrustFaceDto buildFluxEntreeTrustFaceDto(String image_portrait, String base64_selfie) {
        FluxEntreeTrustFaceDto fluxEntreeTrustFaceDto = new FluxEntreeTrustFaceDto();

        // Mocking data for FluxEntreeTrustIdDto
        fluxEntreeTrustFaceDto.setCodeClient("12345"); // Setting codeClient
        fluxEntreeTrustFaceDto.setDocumentFace(image_portrait);
        fluxEntreeTrustFaceDto.setSelfie(base64_selfie);

        return fluxEntreeTrustFaceDto;

    }





}


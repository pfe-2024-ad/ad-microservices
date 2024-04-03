package com.eai.client_service.service;

import com.eai.client_service.dto.InfoClientRequest;
import com.eai.client_service.dto.mocks.ocr.ClientResponseOcrStatut;
import com.eai.client_service.dto.mocks.ocr.trustface.FluxEntreeTrustFaceDto;
import com.eai.client_service.dto.mocks.ocr.trustface.FluxSortieTrustFaceDto;
import com.eai.client_service.dto.mocks.ocr.trustid.FluxEntreeTrustIdDto;
import com.eai.client_service.dto.mocks.ocr.trustid.FluxSortieTrustIdDto;
import com.eai.client_service.outils.enums.OcrStatus;
import com.eai.openfeignservice.config.ConfigClient;
import com.eai.openfeignservice.config.ParamDto;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
public class OcrService {

    private final ClientService clientService;
    private final ConfigClient configClient;


    public Object getCinInfos(MultipartFile file1, MultipartFile file2, MultipartFile file3, Integer id) {
        String base64_RECTO = "";
        String base64_VERSO = "";
        String base64_SELFIE = "";

        MultipartFile[] files = new MultipartFile[]{file1, file2, file3};


        for (int i = 0; i < files.length; i++) {
            try {
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


        FluxEntreeTrustFaceDto fluxEntreeTrustFaceDto = buildFluxEntreeTrustFaceDto(
                responseEntityId.getBody().getDocumentDto().getImagePortrait(), base64_SELFIE);
        HttpEntity<FluxEntreeTrustFaceDto> requestEntityFace = new HttpEntity<>(fluxEntreeTrustFaceDto, headers);
        ResponseEntity<FluxSortieTrustFaceDto> responseEntityFace = restTemplate.postForEntity("http://localhost:7777/agd/client-service/mocks/ocr/trustface", requestEntityFace, FluxSortieTrustFaceDto.class);

        //get similarity value from FluxSortieTrustFace OCR
        Integer similarityOcr = Integer.parseInt(responseEntityFace.getBody().getResult().getResults().getSimilarity());

        //get similarity value from configuration_service
        ParamDto paramDto = ParamDto.builder()
                .name("SIMILARITY").build();
        Integer getSimilarityFromConfig = Integer.parseInt(configClient.getParam(paramDto).getValue());


        if(similarityOcr >= getSimilarityFromConfig ) {

           InfoClientRequest infoClientRequest = InfoClientRequest.builder()
                .idClient(id)
                .nom(responseEntityId.getBody().getDocumentDto().getNom())
                .prenom(responseEntityId.getBody().getDocumentDto().getPrenom())
                .dateNaissance(responseEntityId.getBody().getDocumentDto().getDateNaissance())
                .cin(responseEntityId.getBody().getDocumentDto().getNumeroPersonel())
                .adresseResidence(responseEntityId.getBody().getDocumentDto().getAddress())
                .build();
           clientService.updateInfoClient(infoClientRequest);

           return clientService.getClient(infoClientRequest.getIdClient());
        } else {
            ClientResponseOcrStatut clientResponseOcrStatut = ClientResponseOcrStatut.builder()
                    .status(OcrStatus.ERROR.getLabel()).build();
            return clientResponseOcrStatut;
        }

    }

    private FluxEntreeTrustIdDto buildFluxEntreeTrustIdDto(String base64_recto, String base64_verso) {
        FluxEntreeTrustIdDto fluxEntreeTrustIdDto = new FluxEntreeTrustIdDto();

        fluxEntreeTrustIdDto.setCodeClient("12345"); // Setting codeClient

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

        fluxEntreeTrustIdDto.setDocument(documents);

        return fluxEntreeTrustIdDto;

    }

    private FluxEntreeTrustFaceDto buildFluxEntreeTrustFaceDto(String image_portrait, String base64_selfie) {
        FluxEntreeTrustFaceDto fluxEntreeTrustFaceDto = new FluxEntreeTrustFaceDto();

        fluxEntreeTrustFaceDto.setCodeClient("12345"); // Setting codeClient
        fluxEntreeTrustFaceDto.setDocumentFace(image_portrait);
        fluxEntreeTrustFaceDto.setSelfie(base64_selfie);

        return fluxEntreeTrustFaceDto;

    }





}


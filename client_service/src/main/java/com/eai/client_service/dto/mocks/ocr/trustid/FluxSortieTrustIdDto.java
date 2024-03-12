package com.eai.client_service.dto.mocks.ocr.trustid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FluxSortieTrustIdDto {

    private String codeRetour;
    private String messageRetour;
    private String codeClient;
    private DocumentDto documentDto;

    //Inner class representing the DocumentDto object
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DocumentDto {
        private String codePaysDelivrance;
        private String typeDocument;
        private String numeroDocument;
        private String dateExpiration;
        private String dateNaissance;
        private String nom;
        private String prenom;
        private String lieuNaissance;
        private String numeroPersonel;//cin
        private String nationalite;
        private String codeNationalite;
        private String sexe;
        private String address;
        private String imagePortrait;









    }



}

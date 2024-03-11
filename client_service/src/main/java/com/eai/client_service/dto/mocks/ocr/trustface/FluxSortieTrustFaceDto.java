package com.eai.client_service.dto.mocks.ocr.trustface;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FluxSortieTrustFaceDto {

    private String codeRetour;
    private String messageRetour;
    private String codeClient;
    private Resultat result;

    //Inner class representing the DocumentDto object
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Resultat {
        private String errorCode; //0 si tout ok
        private String code; //0 si tout ok
        private Integer status; //0 si tout ok
        private ResultatComparaison results;

        //Inner class representing the Outputs object
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        public static class ResultatComparaison {
            private String first;
            private String firstFaceIndex;
            private String firstIndex;
            private String score;
            private String secondFaceIndex;
            private String secondIndex;
            private String similarity;

        }
    }
}

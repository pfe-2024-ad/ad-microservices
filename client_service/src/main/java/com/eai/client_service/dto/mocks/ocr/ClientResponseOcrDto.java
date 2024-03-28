package com.eai.client_service.dto.mocks.ocr;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientResponseOcrDto {

    private String similarity;

    private String nom;

    private String prenom;

    private String dateNaissance;

    private String cin;

    private String adresseResidence;

}

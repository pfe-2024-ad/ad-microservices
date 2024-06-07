package com.eai.client_service.dto.mocks.ocr;

import com.eai.openfeignservice.user.outils.enums.PackName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientResponseOcrDto {

    private String status;

    private String nom;

    private String prenom;

    private String email;

    private String phone;

    private String dateNaissance;

    private String cin;

    private String adresseResidence;

    private String ville;

    private String agence;

    private PackName pack;

}

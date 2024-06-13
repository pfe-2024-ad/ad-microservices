package com.eai.client_service.dto.mocks.ocr;

import com.eai.openfeignservice.user.outils.enums.PackName;
import com.eai.openfeignservice.user.outils.enums.PackOffres;
import com.eai.openfeignservice.user.outils.enums.Services;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private List<PackOffres> offres;

    private List<Services> services;

    private String dateCreation;
}

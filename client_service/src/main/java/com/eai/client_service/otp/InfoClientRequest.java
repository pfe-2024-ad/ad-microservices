package com.eai.client_service.otp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InfoClientRequest {

    private Integer idClient;

    private String nom;

    private String prenom;

    private String dateNaissance;

    private String cin;

    private String adresseResidence;

    private String ville;

    private String profession;

    private String codePostal;

    private Boolean mobiliteBancaire;

    private String villeAgence;

    private String adresseAgence;

}

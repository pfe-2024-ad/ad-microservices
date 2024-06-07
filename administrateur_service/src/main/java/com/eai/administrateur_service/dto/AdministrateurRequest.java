package com.eai.administrateur_service.dto;


import com.eai.openfeignservice.administrateur.outils.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdministrateurRequest {

    private String nom;

    private String prenom;

    private String motDePasse;

    private String codeSas;

    private String email;

}

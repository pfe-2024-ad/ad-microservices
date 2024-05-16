package com.eai.openfeignservice.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientResponseForPayment {

        private Integer idClient;

        private String email;

        private String nom;

        private String prenom;

        private String numTel;

        private String adresseResidence;

        private String ville;

        private String codePostal;

        private String dateCreation;

        private String country;



}


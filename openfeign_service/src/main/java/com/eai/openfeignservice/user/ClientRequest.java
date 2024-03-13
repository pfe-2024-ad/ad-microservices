package com.eai.openfeignservice.user;

import com.eai.openfeignservice.user.outils.enums.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientRequest {
    private Integer idClient;
    private String email;
    private ClientProfil profil;

    private String userInput;

    private PackName nomPack;
    private PackType typePack;
    private List<PackOffres> offres;
    private List<CarteName> nomCarte;
    private List<Boolean> sendCarte;
    private List<Services> services;


    private String indicatifTel;
    private String numTel;

}
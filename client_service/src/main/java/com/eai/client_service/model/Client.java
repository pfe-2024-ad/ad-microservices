package com.eai.client_service.model;

import com.eai.client_service.outils.enums.ClientStatus;
import javax.persistence.*;

import com.eai.openfeignservice.user.outils.enums.ClientProfil;
import lombok.*;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Client {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    private Integer id;

    @Column(name="email")
    private String email;

    @Column(name="profil")
    @Enumerated(EnumType.STRING)
    private ClientProfil profil;

    @Column(name="indicatif_tel")
    private String indicatifTel;

    @Column(name="num_tel")
    private String numTel;

    @Column(name="client_status")
    @Enumerated(EnumType.STRING)
    private ClientStatus clientStatus;

    @Column(name="nom")
    private String nom;

    @Column(name="prenom")
    private String prenom;

    @Column(name="date_naissance")
    private String dateNaissance;

    @Column(name="cin")
    private String cin;

    @Column(name="profession")
    private String profession;

    @Column(name="adresse_residence")
    private String adresseResidence;

    @Column(name="code_postal")
    private String codePostal;

    @Column(name="ville")
    private String ville;

    @Column(name="mobilite_bancaire")
    private Boolean mobiliteBancaire;


    @Column(name="ville_agence")
    private String villeAgence;

    @Column(name="adresse_agence")
    private String adresseAgence;



    public Client(String email, ClientStatus clientStatus, ClientProfil profil) {
        this.email = email;
        this.clientStatus = clientStatus;
        this.profil= profil;
    }

}
package com.eai.client_service.model;

import com.eai.client_service.conf.StringListConverter;
import com.eai.openfeignservice.user.outils.enums.*;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pack {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "id_client", referencedColumnName = "id")
    private Client client;

    @Column(name="nom_pack")
    @Enumerated(EnumType.STRING)
    private PackName nomPack;

    @Column(name="type_pack")
    @Enumerated(EnumType.STRING)
    private PackType typePack;

    @Convert(converter = StringListConverter.class)
    @Column(name="offres")
    private List<PackOffres> offres;

    @Convert(converter = StringListConverter.class)
    @Column(name="nom_carte")
    private List<CarteName> nomCarte;

    @Convert(converter = StringListConverter.class)
    @Column(name="send_carte")
    private List<Boolean> sendCarte;

    @Convert(converter = StringListConverter.class)
    @Column(name="services")
    private List<Services> services;

    public Pack(PackName nomPack, PackType typePack, List<PackOffres> offres, List<CarteName> nomCarte, List<Boolean> sendCarte, List<Services> services) {
        this.nomPack = nomPack;
        this.typePack = typePack;
        this.offres = offres;
        this.nomCarte = nomCarte;
        this.sendCarte = sendCarte;
        this.services = services;
    }
}

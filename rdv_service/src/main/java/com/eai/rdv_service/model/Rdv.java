package com.eai.rdv_service.model;

import com.eai.rdv_service.outils.enums.RdvStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Rdv {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="id_client")
    private Integer idClient;

    @Column(name="date")
    private String date;

    @Column(name="heure")
    private String heure;

    @Column(name="status_rdv")
    @Enumerated(EnumType.STRING)
    private RdvStatus rdvStatus;

    @Column(name="id_agent")
    private Integer idAgent;

}

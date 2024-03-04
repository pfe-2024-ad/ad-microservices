package com.eai.client_service.model;

import com.eai.client_service.outils.enums.ClientStatus;
import javax.persistence.*;
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

    @Column(name="indicatif_tel")
    private String indicatifTel;

    @Column(name="num_tel")
    private String numTel;

    @Column(name="client_status")
    @Enumerated(EnumType.STRING)
    private ClientStatus clientStatus;


    public Client(String email, ClientStatus clientStatus) {
        this.email = email;
        this.clientStatus = clientStatus;
    }

}
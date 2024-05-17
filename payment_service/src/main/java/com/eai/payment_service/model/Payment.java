package com.eai.payment_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(name="id_client")
    private Integer idClient;

    @Column(name="amount")
    private String amount;

    @Column(name="date_versement")
    private String date;

    @Column(name="trans_id")
    private String transId;


}

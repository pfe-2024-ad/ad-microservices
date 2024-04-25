package com.eai.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer idClient;
    private Double amount;
    private Date date_versement;
    private String oid;

}
package com.eai.rdv_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RdvDto {

    private Integer idClient;
    private String date;
    private String heure;
}

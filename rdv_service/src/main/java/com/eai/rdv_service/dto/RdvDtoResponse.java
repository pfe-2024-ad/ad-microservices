package com.eai.rdv_service.dto;

import com.eai.rdv_service.outils.enums.RdvStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RdvDtoResponse {

    private String date;
    private String heure;
    private RdvStatus rdvStatus;
    private Integer idAgent;


}

package com.eai.administrateur_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CongeRequest {

    private Integer idAgent;

    private LocalDate dateDebut;

    private LocalDate dateFin;

}


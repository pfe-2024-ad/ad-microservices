package com.eai.openfeignservice.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientRequest {
    private Integer idClient;
    private String email;
    private String indicatifTel;
    private String numTel;

}
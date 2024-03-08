package com.eai.openfeignservice.user;

import com.eai.openfeignservice.user.outils.enums.ClientProfil;
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
    private ClientProfil profil;
    private String indicatifTel;
    private String numTel;

}
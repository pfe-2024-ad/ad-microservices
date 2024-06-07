package com.eai.openfeignservice.administrateur;

import com.eai.openfeignservice.administrateur.outils.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminResponseForSecurity {


    private String email;

    private Role role;

    private String motDePasse;
}

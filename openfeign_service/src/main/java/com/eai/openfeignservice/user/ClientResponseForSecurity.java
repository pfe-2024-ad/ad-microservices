package com.eai.openfeignservice.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientResponseForSecurity {
    private Integer idClient;
    private String email;
    private String clientStep;
}

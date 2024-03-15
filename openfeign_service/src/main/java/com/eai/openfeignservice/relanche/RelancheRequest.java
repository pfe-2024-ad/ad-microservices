package com.eai.openfeignservice.relanche;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RelancheRequest {
    private Integer idClient;

}
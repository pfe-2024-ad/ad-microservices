package com.eai.client_service.dto.mocks.ocr.trustface;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FluxEntreeTrustFaceDto {

    private String codeClient;
    private String documentFace;
    private String selfie;

}

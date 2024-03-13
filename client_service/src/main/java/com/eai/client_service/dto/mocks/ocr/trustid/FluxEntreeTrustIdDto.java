package com.eai.client_service.dto.mocks.ocr.trustid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FluxEntreeTrustIdDto {

    private String codeClient;
    private List<Document> document;

    //Inner class representing the Document object
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Document {
        private String base64DocumentString;
        private int pageIndex;
        private String fileFormat;
        private String lightingMode;
        private String referenceDoc;
    }

}

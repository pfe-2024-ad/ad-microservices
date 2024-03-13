package com.eai.client_service.mocks.ocr.trustface;

import com.eai.client_service.dto.mocks.ocr.trustface.FluxEntreeTrustFaceDto;
import com.eai.client_service.dto.mocks.ocr.trustface.FluxSortieTrustFaceDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("mocks/ocr/trustface")
public class TrustFaceController {
    private final TrustFaceService trustFaceService;

    @PostMapping
    public ResponseEntity<FluxSortieTrustFaceDto> trustFace(@RequestBody FluxEntreeTrustFaceDto fluxEntreeTrustFaceDto) {
        FluxSortieTrustFaceDto fluxSortieTrustFaceDto = trustFaceService.mockedTrustFaceResponse(fluxEntreeTrustFaceDto);
        return new ResponseEntity<>(fluxSortieTrustFaceDto, HttpStatus.OK);
    }
}

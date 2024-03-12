package com.eai.client_service.mocks.ocr.trustid;

import com.eai.client_service.dto.mocks.ocr.trustid.FluxEntreeTrustIdDto;
import com.eai.client_service.dto.mocks.ocr.trustid.FluxSortieTrustIdDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("mocks/ocr/trustid")
public class TrustIdController {

    private final TrustIdService trustIdService;

    @PostMapping
    public ResponseEntity<FluxSortieTrustIdDto> trustId(@RequestBody FluxEntreeTrustIdDto fluxEntreeTrustIdDto) {
        FluxSortieTrustIdDto fluxSortieTrustIdDto = trustIdService.mockedTrustIdResponse(fluxEntreeTrustIdDto);
        return new ResponseEntity<>(fluxSortieTrustIdDto, HttpStatus.OK);
    }

}

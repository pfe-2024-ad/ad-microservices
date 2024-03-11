package com.eai.client_service.mocks.ocr.trustface;

import com.eai.client_service.dto.mocks.ocr.trustface.FluxEntreeTrustFaceDto;
import com.eai.client_service.dto.mocks.ocr.trustface.FluxSortieTrustFaceDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TrustFaceService {
    public FluxSortieTrustFaceDto mockedTrustFaceResponse(FluxEntreeTrustFaceDto fluxEntreeTrustFaceDto) {

        FluxSortieTrustFaceDto fluxSortieTrustFaceDto = new FluxSortieTrustFaceDto();
        fluxSortieTrustFaceDto.setCodeRetour("00000");
        fluxSortieTrustFaceDto.setMessageRetour("success");
        fluxSortieTrustFaceDto.setCodeClient("mockedCodeClient");

        FluxSortieTrustFaceDto.Resultat result = new FluxSortieTrustFaceDto.Resultat();
        result.setErrorCode("0");
        result.setCode("0");
        result.setStatus(0);

        fluxSortieTrustFaceDto.setResult(result);

        FluxSortieTrustFaceDto.Resultat.ResultatComparaison resultatComparaison = new FluxSortieTrustFaceDto.Resultat.ResultatComparaison();
        resultatComparaison.setFirst("dd");
        resultatComparaison.setFirstFaceIndex("dd");
        resultatComparaison.setFirstFaceIndex("ddd");
        resultatComparaison.setScore("Dd");
        resultatComparaison.setSecondFaceIndex("d");
        resultatComparaison.setSecondIndex("dd");
        resultatComparaison.setSimilarity("100%");

        result.setResults(resultatComparaison);
        return fluxSortieTrustFaceDto;
    }
}

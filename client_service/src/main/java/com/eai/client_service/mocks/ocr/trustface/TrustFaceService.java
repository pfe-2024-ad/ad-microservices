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
        resultatComparaison.setFirst("fakeFirst");
        resultatComparaison.setFirstFaceIndex("fakeFirstFace");
        resultatComparaison.setScore("fakeScore");
        resultatComparaison.setSecondFaceIndex("fakeSecondFace");
        resultatComparaison.setSecondIndex("fakeSecondIndex");
        resultatComparaison.setSimilarity("80");

        result.setResults(resultatComparaison);
        return fluxSortieTrustFaceDto;
    }
}

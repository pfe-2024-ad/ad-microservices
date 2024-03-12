package com.eai.client_service.mocks.ocr.trustid;


import com.eai.client_service.dto.mocks.ocr.trustid.FluxEntreeTrustIdDto;
import com.eai.client_service.dto.mocks.ocr.trustid.FluxSortieTrustIdDto;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class TrustIdService {


    public FluxSortieTrustIdDto mockedTrustIdResponse(FluxEntreeTrustIdDto fluxEntreeTrustIdDto) {

        FluxSortieTrustIdDto fluxSortieTrustIdDto = new FluxSortieTrustIdDto();
        fluxSortieTrustIdDto.setCodeRetour("00000");
        fluxSortieTrustIdDto.setMessageRetour("success");
        fluxSortieTrustIdDto.setCodeClient("mockedCodeClient");

        FluxSortieTrustIdDto.DocumentDto documentDto = new FluxSortieTrustIdDto.DocumentDto();
        documentDto.setCodePaysDelivrance("MAR");
        documentDto.setTypeDocument("FAKE_TYPE_DOCUMENT");
        documentDto.setNumeroDocument("123456789");
        documentDto.setDateExpiration("01/01/2030");
        documentDto.setDateNaissance("01/01/2000");
        documentDto.setNom("FakeName");
        documentDto.setPrenom("FakePrenom");
        documentDto.setLieuNaissance("FakeCity");
        documentDto.setNumeroPersonel("fakePersonalNumber");
        documentDto.setNationalite("FakeNationality");
        documentDto.setCodeNationalite("FKN");
        documentDto.setSexe("M");
        documentDto.setAddress("Fake Address");
        documentDto.setImagePortrait("fakeBase64ImageString");

        fluxSortieTrustIdDto.setDocumentDto(documentDto);

        return fluxSortieTrustIdDto;
    }
}
package com.eai.rdv_service.controller;

import com.eai.rdv_service.dto.RdvDto;
import com.eai.rdv_service.dto.RdvDtoResponse;
import com.eai.rdv_service.service.RdvService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class RdvController {

    private final RdvService rdvService;

    @PostMapping("/create-rdv")
    public void createRdv(@RequestBody RdvDto rdvDto){
        rdvService.createRdv(rdvDto);
    }


    @PostMapping("/rdv-by-date")
    public List<RdvDtoResponse> getRdvByDate(@RequestBody RdvDto rdvDto){
        return rdvService.getRdvByDate(rdvDto.getDate());
    }


}

package com.eai.configuration_service.controller;

import com.eai.configuration_service.service.ParamService;
import com.eai.openfeignservice.config.ParamDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ParamController {

    private final ParamService paramService;


    @PostMapping("/get-param")
    public ParamDto getParam(@RequestBody ParamDto paramDto){
        return paramService.getParam(paramDto);
    }



}

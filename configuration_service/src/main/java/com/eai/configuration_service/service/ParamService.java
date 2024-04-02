package com.eai.configuration_service.service;

import com.eai.configuration_service.model.Param;
import com.eai.configuration_service.repository.ParamRepository;
import com.eai.openfeignservice.config.ParamDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParamService {

    private final ParamRepository paramRepository;


    public ParamDto getParam(ParamDto paramDto){
        Param param =  paramRepository.findParamByName(paramDto.getName());
        ParamDto paramResponse = ParamDto.builder()
                .name(param.getName())
                .value(param.getValue())
                .build();
        return  paramResponse;
    }





}

package com.eai.client_service.controller;

import com.eai.client_service.dto.InfoClientRequest;
import com.eai.client_service.service.ClientService;
import com.eai.openfeignservice.user.ClientRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ClientController {

   private final ClientService clientService;

   @PostMapping("email")
   public Integer saveEmail(@RequestBody ClientRequest clientRequest){

       return clientService.saveClient(clientRequest);
   }

   @PostMapping("phone")
    public String addPhone(@RequestBody ClientRequest clientRequest){

       return clientService.addPhone(clientRequest.getIdClient(), clientRequest.getIndicatifTel(), clientRequest.getNumTel());
   }

    @PostMapping("update-info-client")
    public String updateInfoClient(@RequestBody InfoClientRequest infoClientRequest){

        return clientService.updateInfoClient(infoClientRequest);
    }

    @PostMapping("agence")
    public String addAgence(@RequestBody InfoClientRequest infoClientRequest){

        return clientService.addAgence(infoClientRequest);
    }


    @PostMapping("check-client")
    public Boolean isClientExist(@RequestBody ClientRequest clientRequest){
       return clientService.isClientExist(clientRequest.getEmail());
    }


}

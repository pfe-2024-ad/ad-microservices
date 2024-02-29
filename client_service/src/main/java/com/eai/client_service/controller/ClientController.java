package com.eai.client_service.controller;

import com.eai.client_service.service.ClientService;
import com.eai.openfeignservice.user.ClientRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ClientController {

   private final ClientService clientService;

   @PostMapping("email")
   public Integer saveEmail(@RequestBody ClientRequest request){

       return clientService.saveClient(request.getEmail());
   }

   @PostMapping("phone")
    public String addPhone(@RequestBody ClientRequest request){

       return clientService.addPhone(request.getIdClient(), request.getIndicatifTel(), request.getNumTel());
   }


    @PostMapping("check-client")
    public Boolean isClientExist(@RequestBody ClientRequest request){
       return clientService.isClientExist(request.getEmail());
    }


}

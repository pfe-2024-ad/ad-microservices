package com.eai.client_service.controller;

import com.eai.client_service.dto.InfoClientRequest;
import com.eai.client_service.dto.mocks.ocr.ClientResponseOcrDto;
import com.eai.client_service.service.ClientService;
import com.eai.openfeignservice.user.ClientRequest;

import com.eai.openfeignservice.user.ClientResponseForPayment;
import com.eai.openfeignservice.user.ClientResponseForRelanche;
import com.eai.openfeignservice.user.ClientResponseForSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class ClientController {

   private final ClientService clientService;

   @PostMapping("create-client")
   public Integer saveClient(@RequestBody ClientRequest clientRequest){

       return clientService.saveClient(clientRequest);
   }

   @PostMapping("add-client-phone")
    public String addPhone(@RequestBody ClientRequest clientRequest){

       return clientService.addPhone(clientRequest.getIdClient(), clientRequest.getIndicatifTel(), clientRequest.getNumTel());
   }

    @PostMapping("add-client-information")
    public String updateInfoClient(@RequestBody InfoClientRequest infoClientRequest){

        return clientService.updateInfoClient(infoClientRequest);
    }

    @PostMapping("add-client-agency-infos")
    public String addAgence(@RequestBody InfoClientRequest infoClientRequest){

        return clientService.addAgence(infoClientRequest);
    }


    @PostMapping("check-client-if-exist")
    public Boolean isClientExist(@RequestBody ClientRequest clientRequest){
        return clientService.isClientExist(clientRequest);
    }

    @GetMapping("get-clients-need-relanche")
    public List<ClientResponseForRelanche> getClientForRelanche(){
        return clientService.getClientForRelanche();
    }


    @PostMapping("get-email-security")
    public ClientResponseForSecurity getClientForSecurity(@RequestBody String email){
        return clientService.getEmailForSecurity(email);
    }

    @PostMapping("get-client-step")
    ClientResponseForSecurity getClientStep(@RequestBody ClientRequest request){
        return clientService.getClientStep(request);
    }

    @PostMapping("get-client")
    public ClientResponseOcrDto getClient(@RequestBody ClientRequest request) {
        return clientService.getClient(request.getIdClient());
    }

    @PostMapping("set-client-step")
    public void setClientStep(@RequestBody ClientRequest request){
        clientService.setNewStep(request.getIdClient(), request.getStep());
    }

    @PostMapping("get-client-for-payment")
    public ClientResponseForPayment getClientForPayment(@RequestBody Integer id){
        return clientService.getClientForPayment(id);
    }


}

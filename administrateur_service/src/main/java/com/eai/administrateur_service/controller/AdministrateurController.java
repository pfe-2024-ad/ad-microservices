package com.eai.administrateur_service.controller;

import com.eai.administrateur_service.dto.AdministrateurRequest;
import com.eai.administrateur_service.dto.CongeRequest;
import com.eai.openfeignservice.administrateur.DateRequest;
import com.eai.administrateur_service.service.AdministrateurService;
import com.eai.openfeignservice.administrateur.AdminResponseForSecurity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AdministrateurController {

    private final AdministrateurService administrateurService;
/*
    @PostConstruct //pour runner cette methode lorsque je run l'app
    public void initRoleAndUser(){
        administrateurService.initAdmin();
    }
*/

    @PostMapping("/admin/create-agent")
    public void createAgent(@RequestBody AdministrateurRequest administrateurRequest){
         administrateurService.createAgent(administrateurRequest);
    }

    @PostMapping("/admin/create-conge")
    public void createConge(@RequestBody CongeRequest congeRequest){
        administrateurService.createConge(congeRequest);
    }

    @PostMapping("/api/get-nbr-agent-available")
    public Integer getNbrAgentAvailableByDate(@RequestBody DateRequest dateRequest) {
        return administrateurService.getNbrAgentAvailableByDate(dateRequest.getDateRdv());
    }

    @PostMapping("/api/get-available-agents")
    public List<Integer> getAvailableAgents(@RequestBody DateRequest dateRequest) {
        return administrateurService.getAvailableAgents(dateRequest.getDateRdv());
    }

    @PostMapping("/api/get-admin-for-security")
    public AdminResponseForSecurity getAdminForSecurity(@RequestBody String email){
        return administrateurService.getAdminForSecurity(email);
    }

}

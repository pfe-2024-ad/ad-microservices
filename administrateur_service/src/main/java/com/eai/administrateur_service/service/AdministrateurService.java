package com.eai.administrateur_service.service;


import com.eai.administrateur_service.dto.AdministrateurRequest;
import com.eai.administrateur_service.dto.CongeRequest;
import com.eai.administrateur_service.model.Administrateur;
import com.eai.administrateur_service.model.CongeAgent;
import com.eai.administrateur_service.repository.AdministrateurRepository;
import com.eai.administrateur_service.repository.CongeRepository;
import com.eai.openfeignservice.administrateur.AdminResponseForSecurity;
import com.eai.openfeignservice.administrateur.outils.enums.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdministrateurService {

    private final AdministrateurRepository administrateurRepository;
    private final CongeRepository congeRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+[]{}|;:,.<>?";

    public void initAdmin() {
        Administrateur admin = new Administrateur();
        admin.setNom("admin");
        admin.setPrenom("admin");
        admin.setEmail("admin@example.com");
        admin.setRole(Role.ADMIN);
        admin.setCodeSas("codeSas");
        admin.setMotDePasse(getEncodedPassword("admin@123"));
        administrateurRepository.save(admin);
    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public void createAgent(AdministrateurRequest administrateurRequest){
        Administrateur administrateur = Administrateur.builder()
                .nom(administrateurRequest.getNom())
                .prenom(administrateurRequest.getPrenom())
                .email(administrateurRequest.getEmail())
                .codeSas(administrateurRequest.getCodeSas())
                .role(Role.AGENT)
                .motDePasse(getEncodedPassword(administrateurRequest.getMotDePasse()))
                .build();
        administrateurRepository.save(administrateur);
    }

    public void createConge(CongeRequest congeRequest){
        CongeAgent conge = CongeAgent.builder()
                .idAgent(congeRequest.getIdAgent())
                .dateDebut(congeRequest.getDateDebut())
                .dateFin(congeRequest.getDateFin())
                .build();
        congeRepository.save(conge);
    }

    public Integer getNbrAgentAvailableByDate(String date){
        List<CongeAgent> conges = congeRepository.findCongeByDate(LocalDate.parse(date));
        List<Administrateur> agents = administrateurRepository.findByRole(Role.AGENT);
        if(!conges.isEmpty()){
           return agents.size()-conges.size();
        } else {
            return agents.size();
        }
    }

    public List<Integer> getAvailableAgents (String date){
        List<CongeAgent> conges = congeRepository.findCongeByDate(LocalDate.parse(date));
        List<Administrateur> agents = administrateurRepository.findByRole(Role.AGENT);

        List<Integer> unavailableAgents = new ArrayList<>();
        List<Integer> availableAgents = new ArrayList<>();

        for(Administrateur agent: agents){
            availableAgents.add(agent.getId());
        }
        log.info("Available agents by dat: "+availableAgents.toString());

        if(!conges.isEmpty()) {
            for (CongeAgent conge : conges) {
                unavailableAgents.add(conge.getIdAgent());
            }
        }
        log.info("Unavailable agents by dat: "+unavailableAgents.toString());

        availableAgents.removeAll(unavailableAgents);
        return availableAgents;
    }


    public AdminResponseForSecurity getAdminForSecurity(String email){
        Administrateur admin = administrateurRepository.findByEmail(email);
        AdminResponseForSecurity adminResponseForSecurity = new AdminResponseForSecurity();
        if(admin != null) {
            adminResponseForSecurity.setEmail(admin.getEmail());
            adminResponseForSecurity.setRole(admin.getRole());
            adminResponseForSecurity.setMotDePasse(admin.getMotDePasse());
        } else {
            adminResponseForSecurity.setEmail(null);
            adminResponseForSecurity.setRole(null);
            adminResponseForSecurity.setMotDePasse(null);
        }
        return adminResponseForSecurity;
    }



}

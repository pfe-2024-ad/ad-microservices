package com.eai.rdv_service.service;

import com.eai.openfeignservice.administrateur.AdminClient;
import com.eai.openfeignservice.administrateur.DateRequest;
import com.eai.openfeignservice.user.ClientRequest;
import com.eai.openfeignservice.user.UserClient;
import com.eai.openfeignservice.user.outils.enums.ClientStep;
import com.eai.rdv_service.dto.RdvDto;
import com.eai.rdv_service.dto.RdvDtoResponse;
import com.eai.rdv_service.model.Rdv;
import com.eai.rdv_service.outils.enums.RdvStatus;
import com.eai.rdv_service.repository.RdvRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class RdvService {

    private final RdvRepository rdvRepository;
    private final UserClient userClient;
    private final AdminClient adminClient;

    public void createRdv(RdvDto rdvDto){

        DateRequest dateRequest = DateRequest.builder()
                .dateRdv(rdvDto.getDate())
                .build();
        List<Integer> availableAgentsDate = adminClient.getAvailableAgents(dateRequest);
        log.info("availableAgentsDate: "+availableAgentsDate.toString());

        List<Integer> idAgentsHasRdv = getRdvByDateAndHeure(rdvDto.getDate(), rdvDto.getHeure());
        log.info("AgentsHasRdvDateAndHeure: "+idAgentsHasRdv.toString());

        availableAgentsDate.removeAll(idAgentsHasRdv);
        log.info("availableAgentsDateAndHeure: "+availableAgentsDate.toString());

        // Initialiser une map pour stocker le nombre de rendez-vous par agent
        Map<Integer, Integer> agentRdvCountMap = new HashMap<>();

        for (Integer idAgent : availableAgentsDate) {
            int rdvCount = countRdvByAgentAndDate(idAgent, rdvDto.getDate());
            agentRdvCountMap.put(idAgent, rdvCount);
        }
        log.info("liste agent et count: "+agentRdvCountMap.toString());

        // Trouver le nombre minimum de rendez-vous
        int minRdvCount = Collections.min(agentRdvCountMap.values());

        // Filtrer les agents ayant le nombre minimum de rendez-vous
        List<Integer> agentsWithMinRdv = agentRdvCountMap.entrySet().stream()
                .filter(entry -> entry.getValue() == minRdvCount)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        log.info("liste agents with min rdv: "+agentsWithMinRdv.toString());

        // Sélectionner un ID d'agent aléatoire parmi les agents ayant le nombre minimum de rendez-vous
        Random random = new Random();
        int randomAgentWithMinRdv = agentsWithMinRdv.get(random.nextInt(agentsWithMinRdv.size()));

        RdvStatus status;
        if(rdvDto.getDate() == null && rdvDto.getHeure() == null ){
            status = RdvStatus.A_CONVENIR;
        } else {
            status = RdvStatus.A_FAIRE;
        }

        Rdv rdv = Rdv.builder()
                .idClient(rdvDto.getIdClient())
                .date(rdvDto.getDate())
                .heure(rdvDto.getHeure())
                .idAgent(randomAgentWithMinRdv)
                .rdvStatus(status)
                .build();
        rdvRepository.save(rdv);

        ClientRequest clientRequest = ClientRequest.builder()
                .idClient(rdvDto.getIdClient())
                .step(ClientStep.RDV_STEP)
                .build();
        userClient.setClientStep(clientRequest);
    }

    public List<RdvDtoResponse> getRdvByDate(String date){
        List<Rdv> rdvList =  rdvRepository.findByDate(date);
        List<RdvDtoResponse> rdvDtoResponses = new ArrayList<>();
        for(Rdv rdv :rdvList){
            RdvDtoResponse response = RdvDtoResponse.builder()
                    .date(rdv.getDate())
                    .heure(rdv.getHeure())
                    .rdvStatus(rdv.getRdvStatus())
                    .idAgent(rdv.getIdAgent())
                    .build();
            rdvDtoResponses.add(response);
        }
        return rdvDtoResponses;

    }

    private List<Integer> getRdvByDateAndHeure(String date, String heure){
        List<Rdv> rdvList = rdvRepository.findByDateAndHeure(date, heure);
        List<Integer> idAgentsHasRdv = new ArrayList<>();
        for(Rdv rdv :rdvList){
            idAgentsHasRdv.add(rdv.getIdAgent());
        }
        return idAgentsHasRdv;

    }

    // Méthode pour compter le nombre de rendez-vous par agent à une date donnée
    private int countRdvByAgentAndDate(Integer agentId, String date) {
        List<Rdv> rdvs = rdvRepository.findByIdAgentAndDate(agentId, date);
        return rdvs.size();
    }


}

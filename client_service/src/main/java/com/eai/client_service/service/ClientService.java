package com.eai.client_service.service;

import com.eai.client_service.dto.mocks.ocr.ClientResponseOcrDto;
import com.eai.client_service.model.Pack;
import com.eai.client_service.dto.InfoClientRequest;
import com.eai.client_service.outils.enums.*;
import com.eai.client_service.model.Client;
import com.eai.client_service.repository.ClientRepository;
import com.eai.client_service.repository.PackRepository;
import com.eai.openfeignservice.administrateur.outils.enums.Role;
import com.eai.openfeignservice.relanche.RelancheClient;
import com.eai.openfeignservice.relanche.RelancheRequest;
import com.eai.openfeignservice.user.ClientRequest;
import com.eai.openfeignservice.user.ClientResponseForPayment;
import com.eai.openfeignservice.user.ClientResponseForRelanche;
import com.eai.openfeignservice.user.ClientResponseForSecurity;
import com.eai.openfeignservice.user.outils.enums.ClientProfil;
import com.eai.openfeignservice.user.outils.enums.ClientStep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final PackRepository packRepository;
    private final RelancheClient relancheClient;



    public Integer saveClient(ClientRequest clientRequest){
        ClientStatus status = ClientStatus.PRE_PROSPECT;
        ClientStep clientStep = ClientStep.EMAIL_STEP;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateCreation = dateFormat.format(new Date());
        String country;
        if(clientRequest.getProfil() != ClientProfil.MRE){
            country = "Maroc";
        } else {
            country = "Autre";
        }
        Client client = new Client(clientRequest.getEmail(), status, clientRequest.getProfil(), clientStep, dateCreation, country, Role.CLIENT);

        Pack pack = new Pack(clientRequest.getNomPack(), clientRequest.getTypePack(), clientRequest.getOffres(), clientRequest.getNomCarte(),
                clientRequest.getSendCarte(), clientRequest.getServices());

        // Associez d'abord le pack au client
        pack.setClient(client);

        clientRepository.save(client);
        packRepository.save(pack);

        return client.getId();
    }


    public String addPhone(Integer idClient, String indicatiTel, String numTel){

        Optional<Client> clientOptional = clientRepository.findById(idClient);
        if(clientOptional.isPresent()){
            Client client = clientOptional.get(); // Extracting the Client object from Optional
            client.setClientStep(ClientStep.PHONE_STEP);
            client.setClientStatus(ClientStatus.PROSPECT);
            client.setIndicatifTel(indicatiTel);
            client.setNumTel(numTel);
            clientRepository.save(client);

            RelancheRequest relancheRequest = RelancheRequest.builder()
                            .idClient(idClient).build();

            relancheClient.changeRelaunchStatusToNone(relancheRequest);

            return AddPhoneStatus.SUCCESSFUL.getLabel();
        }
        else{
            return AddPhoneStatus.ERROR.getLabel();
        }
    }

    public String updateInfoClient(InfoClientRequest infoClientRequest) {
        Optional<Client> clientOptional = clientRepository.findById(infoClientRequest.getIdClient());
        if (clientOptional.isPresent()) {
            Client client = clientOptional.get(); // Extracting the Client object from Optional
          
            client.setClientStep(infoClientRequest.getStep());
            client.setNom(infoClientRequest.getNom());
            client.setPrenom(infoClientRequest.getPrenom());
            client.setDateNaissance(infoClientRequest.getDateNaissance());
            client.setCin(infoClientRequest.getCin());
            client.setAdresseResidence(infoClientRequest.getAdresseResidence());
            client.setVille(infoClientRequest.getVille());
            client.setProfession(infoClientRequest.getProfession());
            client.setCodePostal(infoClientRequest.getCodePostal());
            client.setMobiliteBancaire(infoClientRequest.getMobiliteBancaire());
            clientRepository.save(client);

            RelancheRequest relancheRequest = RelancheRequest.builder()
                    .idClient(infoClientRequest.getIdClient()).build();

            relancheClient.changeRelaunchStatusToNone(relancheRequest);



            return SaveInfoClientStatus.SUCCESSFUL.getLabel();
        } else {
            return SaveInfoClientStatus.ERROR.getLabel();
        }
    }

    public String addAgence(InfoClientRequest infoClientRequest) {
        Optional<Client> clientOptional = clientRepository.findById(infoClientRequest.getIdClient());
        if (clientOptional.isPresent()) {
            Client client = clientOptional.get(); // Extracting the Client object from Optional
            client.setClientStep(ClientStep.AGENCY_STEP);
            client.setVilleAgence(infoClientRequest.getVilleAgence());
            client.setAdresseAgence(infoClientRequest.getAdresseAgence());
            clientRepository.save(client);


            RelancheRequest relancheRequest = RelancheRequest.builder()
                    .idClient(infoClientRequest.getIdClient()).build();
            relancheClient.changeRelaunchStatusToNone(relancheRequest);

            return SaveInfoClientStatus.SUCCESSFUL.getLabel();
        } else {
            return SaveInfoClientStatus.ERROR.getLabel();
        }
    }

    public ClientResponseOcrDto getClient(Integer id){
        Optional<Client> clientOptional = clientRepository.findById(id);
        if (clientOptional.isPresent()) {
            Client client = clientOptional.get(); // Extracting the Client object from Optional
            ClientResponseOcrDto clientResponseOcrDto = ClientResponseOcrDto.builder()
                    .status(OcrStatus.SUCCESSFUL.getLabel())
                    .nom(client.getNom())
                    .prenom(client.getPrenom())
                    .email(client.getEmail())
                    .phone(client.getIndicatifTel()+client.getNumTel())
                    .cin(client.getCin())
                    .dateNaissance(client.getDateNaissance())
                    .adresseResidence(client.getAdresseResidence())
                    .ville(client.getVille())
                    .agence(client.getAdresseAgence()+" "+client.getVilleAgence())
                    .pack(client.getPack().getNomPack())
                    .offres(client.getPack().getOffres())
                    .services(client.getPack().getServices())
                    .dateCreation(client.getDateCreation())
                    .build();
            return clientResponseOcrDto;
        } else {
            return null;
        }
    }


    public Boolean isClientExist(ClientRequest request){

        Client client = clientRepository.findByEmail(request.getEmail());
        return client != null;

    }

    public ClientResponseForSecurity getClientStep(ClientRequest request){
        Client client = clientRepository.findByEmail(request.getEmail());
        ClientResponseForSecurity clientRequest = ClientResponseForSecurity.builder()
                .idClient(client.getId())
                .clientStep(client.getClientStep().toString())
                .build();
        return clientRequest;
    }

    public List<ClientResponseForRelanche> getClientForRelanche(){
        List<ClientResponseForRelanche> clientResponse = new ArrayList<>();
        List<Client> allClient = clientRepository.findAll();
        for(Client client:allClient){
            if(client.getClientStep() != ClientStep.RDV_STEP){
                ClientResponseForRelanche clientResponseForRelanche = ClientResponseForRelanche.builder()
                        .idClient(client.getId())
                        .email(client.getEmail())
                        .build();
                clientResponse.add(clientResponseForRelanche);
            }
        }
        return clientResponse;
    }

    public ClientResponseForSecurity getEmailForSecurity(String email){

        Client client = clientRepository.findByEmail(email);

        ClientResponseForSecurity clientResponseForSecurity = new ClientResponseForSecurity();
        if(client != null) {
            clientResponseForSecurity.setIdClient(client.getId());
            clientResponseForSecurity.setEmail(client.getEmail());
            clientResponseForSecurity.setRole(client.getRole());
        } else {
            clientResponseForSecurity.setIdClient(null);
            clientResponseForSecurity.setEmail(null);
            clientResponseForSecurity.setRole(null);
        }
        return clientResponseForSecurity;

    }

    public void setNewStep(Integer idClient, ClientStep step){
        Client client = clientRepository.findClientById(idClient);
        client.setClientStep(step);
        clientRepository.save(client);
    }

    public ClientResponseForPayment getClientForPayment(Integer id){
        Optional<Client> clientOptional = clientRepository.findById(id);
        if (clientOptional.isPresent()) {
            Client client = clientOptional.get(); // Extracting the Client object from Optional
            ClientResponseForPayment clientResponseForPayment = ClientResponseForPayment.builder()
                    .idClient(id)
                    .email(client.getEmail())
                    .nom(client.getNom())
                    .prenom(client.getPrenom())
                    .numTel(client.getIndicatifTel()+client.getNumTel())
                    .adresseResidence(client.getAdresseResidence())
                    .ville(client.getVille())
                    .codePostal(client.getCodePostal())
                    .dateCreation(client.getDateCreation())
                    .country(client.getCountry())
                    .build();
            return clientResponseForPayment;
        } else {
            return null;
        }
    }


}

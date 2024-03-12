package com.eai.client_service.service;

import com.eai.client_service.model.Pack;
import com.eai.client_service.dto.InfoClientRequest;
import com.eai.client_service.outils.enums.AddPhoneStatus;
import com.eai.client_service.outils.enums.ClientStatus;
import com.eai.client_service.model.Client;
import com.eai.client_service.outils.enums.SaveInfoClientStatus;
import com.eai.client_service.repository.ClientRepository;
import com.eai.client_service.repository.PackRepository;
import com.eai.openfeignservice.user.ClientRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final PackRepository packRepository;


    public Integer saveClient(ClientRequest clientRequest){
        ClientStatus status = ClientStatus.PRE_PROSPECT;
        Client client = new Client(clientRequest.getEmail(), status, clientRequest.getProfil());
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
            client.setClientStatus(ClientStatus.PROSPECT);
            client.setIndicatifTel(indicatiTel);
            client.setNumTel(numTel);
            clientRepository.save(client);
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
            return SaveInfoClientStatus.SUCCESSFUL.getLabel();
        } else {
            return SaveInfoClientStatus.ERROR.getLabel();
        }
    }

    public String addAgence(InfoClientRequest infoClientRequest) {
        Optional<Client> clientOptional = clientRepository.findById(infoClientRequest.getIdClient());
        if (clientOptional.isPresent()) {
            Client client = clientOptional.get(); // Extracting the Client object from Optional
            client.setVilleAgence(infoClientRequest.getVilleAgence());
            client.setAdresseAgence(infoClientRequest.getAdresseAgence());
            clientRepository.save(client);
            return SaveInfoClientStatus.SUCCESSFUL.getLabel();
        } else {
            return SaveInfoClientStatus.ERROR.getLabel();
        }
    }


    public Boolean isClientExist(String email){

        List<ClientStatus> statuses = Arrays.asList(
                ClientStatus.PRE_PROSPECT,
                ClientStatus.PROSPECT,
                ClientStatus.PROSPECT_FINALISE,
                ClientStatus.EQUIPPED
        );
        Client client = clientRepository.findByEmailAndClientStatusIn(email, statuses);

        return client != null;

    }
}

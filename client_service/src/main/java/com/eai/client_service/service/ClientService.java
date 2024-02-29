package com.eai.client_service.service;

import com.eai.client_service.outils.enums.AddPhoneStatus;
import com.eai.client_service.outils.enums.ClientStatus;
import com.eai.client_service.model.Client;
import com.eai.client_service.repository.ClientRepository;
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


    public Integer saveClient(String email){
        ClientStatus status = ClientStatus.PRE_PROSPECT;
        Client client = new Client(email, status);
        clientRepository.save(client);
        return client.getId();
    }

    public String addPhone(Integer idClient, String indicatiTel, String numTel){

        Optional<Client> client = clientRepository.findById(idClient);
        if(client.isPresent()){
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

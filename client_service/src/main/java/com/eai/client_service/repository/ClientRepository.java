package com.eai.client_service.repository;

import com.eai.client_service.model.Client;
import com.eai.client_service.outils.enums.ClientStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Integer> {
    Client findByEmailAndClientStatusIn(String email, List<ClientStatus> statuses);
    Client findByEmail(String email);

}

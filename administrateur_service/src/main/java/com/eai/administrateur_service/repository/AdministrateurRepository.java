package com.eai.administrateur_service.repository;

import com.eai.administrateur_service.model.Administrateur;
import com.eai.openfeignservice.administrateur.outils.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdministrateurRepository extends JpaRepository<Administrateur, Integer> {
    Administrateur findByEmail(String email);
    List<Administrateur> findByRole(Role role);
}

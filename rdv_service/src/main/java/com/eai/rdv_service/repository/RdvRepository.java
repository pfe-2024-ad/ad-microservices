package com.eai.rdv_service.repository;

import com.eai.rdv_service.model.Rdv;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RdvRepository extends JpaRepository<Rdv, Integer> {
    List<Rdv> findByDate(String date);
    List<Rdv> findByDateAndHeure(String date, String heure);
    List<Rdv> findByIdAgentAndDate(Integer idAgent, String date);

}

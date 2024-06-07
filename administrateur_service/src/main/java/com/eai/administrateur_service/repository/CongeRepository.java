package com.eai.administrateur_service.repository;

import com.eai.administrateur_service.model.CongeAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CongeRepository extends JpaRepository<CongeAgent, Integer> {

    @Query("SELECT c FROM CongeAgent c WHERE :date BETWEEN c.dateDebut AND c.dateFin")
    List<CongeAgent> findCongeByDate(@Param("date") LocalDate date);



}

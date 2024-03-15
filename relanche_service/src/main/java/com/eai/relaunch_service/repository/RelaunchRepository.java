package com.eai.relaunch_service.repository;

import com.eai.relaunch_service.model.Relaunch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RelaunchRepository extends JpaRepository<Relaunch, Integer> {
    Optional<Relaunch> findByIdClient(Integer idClient);
}
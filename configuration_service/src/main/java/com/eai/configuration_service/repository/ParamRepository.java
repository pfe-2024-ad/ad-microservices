package com.eai.configuration_service.repository;

import com.eai.configuration_service.model.Param;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParamRepository extends JpaRepository<Param, Integer> {
    Param findParamByName(String name);
}

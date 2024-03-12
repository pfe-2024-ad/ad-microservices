package com.eai.securityservice.repository;

import com.eai.securityservice.model.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Long> {
    History findTopByEmailOrderByDateGenerationDesc(String email);
    History  findTopByKeyPhoneAndNumPhoneOrderByDateGenerationDesc(String keyPhone, String numPhone);
}

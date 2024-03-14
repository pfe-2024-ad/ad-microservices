package com.eai.securityservice.repository;

import com.eai.securityservice.model.Counter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounterRepository extends JpaRepository<Counter, Long> {
}

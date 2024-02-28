package com.eai.securityservice.repository;


import com.eai.securityservice.model.OtpPhone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpPhoneRepository extends JpaRepository<OtpPhone, Long> {
    // Additional query methods if needed
}

package com.eai.securityservice.repository;

import com.eai.securityservice.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    Otp findByKeyPhoneAndNumPhone(String keyPhone, String numPhone);
    Otp findByEmail(String email);
    Otp findByIdClient(UUID idClient);

}

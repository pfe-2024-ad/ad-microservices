package com.eai.otpservice.repository;


import com.ayman.client_otp.otp_service.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    // Additional query methods if needed
}

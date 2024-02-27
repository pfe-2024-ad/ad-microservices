package com.eai.otpservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "otp")
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_client")
    private Long idClient;

    @Column(name = "otp_code")
    private String storedOtp;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OtpStatus status;

    // Constructors, getters, setters, etc.
}

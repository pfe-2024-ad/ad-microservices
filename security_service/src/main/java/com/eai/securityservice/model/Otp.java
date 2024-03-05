package com.eai.securityservice.model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

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
    private Integer idClient;

    @Column(name = "key_phone")
    private String keyPhone;

    @Column(name = "num_phone")
    private String numPhone;

    @Column(name = "email")
    private String email;

    @Column(name = "counter")
    private Integer counter;

    @Column(name = "date_generation")
    private Date dateGeneration;

    @Column(name = "attempts" )
    private Integer attempts = 0;

    public Otp(String keyPhone, String numPhone, Integer counter, Date dateGeneration) {
        this.keyPhone = keyPhone;
        this.numPhone = numPhone;
        this.counter = counter;
        this.dateGeneration = dateGeneration;

    }
    public Otp(String email, Integer counter, Date dateGeneration) {
        this.email = email;
        this.counter = counter;
        this.dateGeneration = dateGeneration;

    }

    // Constructors, getters, setters, etc.
    public void incrementAttempt() {
        this.attempts++;
    }

}

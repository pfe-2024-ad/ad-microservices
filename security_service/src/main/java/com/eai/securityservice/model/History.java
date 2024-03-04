package com.eai.securityservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "History")
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "keyPhone")
    private String keyPhone;

    @Column(name = "numPhone")
    private String numPhone;

    @Column(name = "email")
    private String email;

    @Column(name = "counter")
    private Integer counter;

    @Column(name = "date_generation")
    private Date dateGeneration;

    @Column(name = "num_generation" )
    private Integer numGeneration = 1;

    public History(String keyPhone, String numPhone, Integer counter, Date dateGeneration) {
        this.keyPhone = keyPhone;
        this.numPhone = numPhone;
        this.counter = counter;
        this.dateGeneration = dateGeneration;

    }
    public History(String email, Integer counter, Date dateGeneration) {
        this.email = email;
        this.counter = counter;
        this.dateGeneration = dateGeneration;

    }

    // Constructors, getters, setters, etc.
    public void incrementNumGeneration() {
        this.numGeneration++;
    }

}

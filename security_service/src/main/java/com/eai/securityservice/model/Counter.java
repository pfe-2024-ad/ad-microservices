package com.eai.securityservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@Entity
@Builder
@NoArgsConstructor
@Component
@AllArgsConstructor
@Data
@Table(name = "counter")
public class Counter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer counter = 0;

    public void incrementCounter() {
        this.counter++;
    }
}

package com.eai.configuration_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Param {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer param_id;

    @Column(name = "param_name", unique = true)
    private String name;

    @Column(name = "param_value")
    private String value;
}

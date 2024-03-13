package com.eai.relaunch_service.model;

import com.eai.relaunch_service.outils.enums.RelaunchMovementStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Relaunch {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    private Integer id;

    @Column(name="id_client")
    private Integer idClient;

    @Column(name="relaunch_status")
    @Enumerated(EnumType.STRING)
    private RelaunchMovementStatus relaunchStatus;
}

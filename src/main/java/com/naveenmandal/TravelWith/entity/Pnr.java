package com.naveenmandal.TravelWith.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Pnr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pnrNo;

    @Column(length = 15, nullable = false)
    private String phoneNo;

    private String trainNo;

    private LocalDate journeyDate;
    private LocalDate journeyEndDate;
}

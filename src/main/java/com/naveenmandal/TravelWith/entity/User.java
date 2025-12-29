package com.naveenmandal.TravelWith.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class User {

    @Id
    @Column(length = 15)
    private String phoneNo;

    private String name;
    private String sourceStation;
    private String destinationStation;

    private LocalTime trainDepartureTime;
    private LocalTime destArrivalTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pnr", referencedColumnName = "pnrNo")
    private Pnr pnr;
}

package com.naveenmandal.TravelWith.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class User {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String name;
    private String sourceStation;
    private String destinationStation;
    private LocalTime trainDepartureTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pnr", referencedColumnName = "pnrNo")
    private Pnr pnr ;
}
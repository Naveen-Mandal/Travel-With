package com.naveenmandal.TravelWith.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "station", indexes = {
        @Index(name = "idx_station_name", columnList = "name")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;
}

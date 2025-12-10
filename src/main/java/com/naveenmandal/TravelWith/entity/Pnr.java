package com.naveenmandal.TravelWith.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Pnr {
    @Id
    private String pnrNo;
    private String phoneNo;
    private String trainNo;
    private LocalDate journeyDate;

    @Override
    public String toString() {
        return "Pnr{" +
                "pnrNo='" + pnrNo + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", trainNo='" + trainNo + '\'' +
                ", journeyDate=" + journeyDate +
                '}';
    }
}
package com.naveenmandal.TravelWith.repository;

import com.naveenmandal.TravelWith.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface UserRepo extends JpaRepository<User, String> {

    // Same train + same date, excluding current PNR
    List<User> findByPnr_TrainNoAndPnr_JourneyDateAndPnr_PnrNoNot(
            String trainNo,
            LocalDate journeyDate,
            String pnrNo
    );

    /*
    List<User> findBySourceStationAndPnr_JourneyDateAndStationTimeAndPnr_PNot(
            String sourceStation,
            LocalDate journeyDate,
            LocalTime trainDepartureTime,
            String pId
    );
*/
//    Same station + same date + same time, excluding current PNR
    @Query("select u from User u where u.sourceStation = :sourceStation and u.pnr.journeyDate = :journeyDate and u.pnr.pnrNo <> :pnrNo and u.trainDepartureTime <= :plusMargin and u.trainDepartureTime >= :minusMargin")
    List<User> findCoPassengers(
            @Param("sourceStation") String sourceStation,
            @Param("journeyDate") LocalDate journeyDate,
            @Param("plusMargin") LocalTime plusMargin,
            @Param("minusMargin") LocalTime minusMargin,
            @Param("pnrNo") String pnrNO
    );
}
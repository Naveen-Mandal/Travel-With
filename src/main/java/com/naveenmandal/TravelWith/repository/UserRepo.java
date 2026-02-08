package com.naveenmandal.TravelWith.repository;

import com.naveenmandal.TravelWith.entity.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public interface UserRepo extends JpaRepository<User, String> {



    @Query("""
      select u from User u
      join fetch u.pnr
      where u.pnr.trainNo = :trainNo
        and u.pnr.journeyDate <= :myEndDate
        and u.pnr.journeyEndDate >= :myStartDate
        and u.phoneNo <> :phoneNo
    """)
    List<User> findSameTrainOverlap(
            @Param("trainNo") String trainNo,
            @Param("myStartDate") LocalDate myStartDate,
            @Param("myEndDate") LocalDate myEndDate,
            @Param("phoneNo") String phoneNo
    );

    @Query("""
      select u from User u
      join fetch u.pnr
      where u.sourceStation = :sourceStation
        and u.pnr.journeyDate <= :myEndDate
        and u.pnr.journeyEndDate >= :myStartDate
        and u.phoneNo <> :phoneNo
        and u.trainDepartureTime <= :plusMargin
        and u.trainDepartureTime >= :minusMargin
    """)
    List<User> findCoPassengersAtSourceOverlap(
            @Param("sourceStation") String sourceStation,
            @Param("myStartDate") LocalDate myStartDate,
            @Param("myEndDate") LocalDate myEndDate,
            @Param("plusMargin") LocalTime plusMargin,
            @Param("minusMargin") LocalTime minusMargin,
            @Param("phoneNo") String phoneNo
    );

    @Query("""
      select u from User u
      join fetch u.pnr
      where u.destinationStation = :destinationStation
        and u.pnr.journeyEndDate = :journeyEndDate
        and u.phoneNo <> :phoneNo
        and u.destArrivalTime <= :plusMargin
        and u.destArrivalTime >= :minusMargin
    """)
    List<User> findCoPassengersAtDestination(
            @Param("destinationStation") String destinationStation,
            @Param("journeyEndDate") LocalDate journeyEndDate,
            @Param("plusMargin") LocalTime plusMargin,
            @Param("minusMargin") LocalTime minusMargin,
            @Param("phoneNo") String phoneNo
    );
}

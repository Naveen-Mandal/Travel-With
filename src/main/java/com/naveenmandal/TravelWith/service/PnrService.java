package com.naveenmandal.TravelWith.service;

import com.naveenmandal.TravelWith.entity.Pnr;
import com.naveenmandal.TravelWith.entity.User;
import com.naveenmandal.TravelWith.repository.PnrRepo;
import com.naveenmandal.TravelWith.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PnrService {

    private final PnrRepo pnrRepo;
    private final UserRepo userRepo;

    public void saveJourney(
            String phoneNo,
            String studentName,
            String sourceStation,
            String destinationStation,
            LocalTime stationTime,
            LocalTime destArrivalTime,
            String trainNo,
            LocalDate journeyDate,
            LocalDate journeyEndDate
    ) {
        Pnr pnr = new Pnr(null, phoneNo, trainNo, journeyDate, journeyEndDate);
        pnrRepo.save(pnr);

        User currentUser = new User(
                phoneNo,
                studentName,
                sourceStation,
                destinationStation,
                stationTime,
                destArrivalTime,
                pnr
        );
        userRepo.save(currentUser);
    }

    public List<User> findMatchesAtSource(
            String phoneNo,
            String sourceStation,
            LocalTime stationTime,
            String trainNo,
            LocalDate journeyDate,
            LocalDate journeyEndDate
    ) {
        Set<User> result = new LinkedHashSet<>();

        if (trainNo != null && !trainNo.isBlank()) {
            result.addAll(userRepo.findSameTrainOverlap(trainNo, journeyDate, journeyEndDate, phoneNo));
        }

        result.addAll(userRepo.findCoPassengersAtSourceOverlap(
                sourceStation,
                journeyDate,
                journeyEndDate,
                stationTime.plusHours(1),
                stationTime.minusHours(1),
                phoneNo
        ));

        return new ArrayList<>(result);
    }

    public List<User> findMatchesAtDestination(
            String phoneNo,
            String destinationStation,
            LocalTime destArrivalTime,
            LocalDate journeyEndDate
    ) {
        return userRepo.findCoPassengersAtDestination(
                destinationStation,
                journeyEndDate,
                destArrivalTime.plusHours(1),
                destArrivalTime.minusHours(1),
                phoneNo
        );
    }
}

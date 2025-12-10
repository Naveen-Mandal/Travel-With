package com.naveenmandal.TravelWith.service;


import com.naveenmandal.TravelWith.entity.Pnr;
import com.naveenmandal.TravelWith.entity.User;
import com.naveenmandal.TravelWith.repository.PnrRepo;
import com.naveenmandal.TravelWith.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class PnrService {

    @Autowired
    private PnrRepo pnrRepo;

    @Autowired
    private UserRepo userRepo;


//      Save current user+PNR and find matching co-travelers.
    public List<User> saveAndFindMatches(String phoneNo,
                                         String name,
                                         String sourceStation,
                                         String destinationStation,
                                         LocalTime stationTime,
                                         String trainNo,
                                         LocalDate journeyDate) {

        //Save PNR
        Pnr pnr = new Pnr(UUID.randomUUID().toString(), phoneNo, trainNo,journeyDate);
        pnrRepo.save(pnr);

        //Save current user linked to this PNR
        User currentUser = new User(UUID.randomUUID().toString(),
                name,
                sourceStation,
                destinationStation,
                stationTime,
                pnr);
        userRepo.save(currentUser);

        //Find matches on same train + same date
        List<User> sameTrain = userRepo.findByPnr_TrainNoAndPnr_JourneyDateAndPnr_PnrNoNot(
                pnr.getTrainNo(),
                pnr.getJourneyDate(),
                pnr.getPnrNo()
        );

        //Find matches on same station + same time + same date
        List<User> sameStation = userRepo.findCoPassengers(
                currentUser.getSourceStation(),
                pnr.getJourneyDate(),
                currentUser.getTrainDepartureTime().plusHours(1),
                currentUser.getTrainDepartureTime().minusHours(1),
                pnr.getPnrNo()
        );

        //Combine and de-duplicate
        Set<User> result = new LinkedHashSet<>();
        result.addAll(sameTrain);
        result.addAll(sameStation);

        return new ArrayList<>(result);
    }
}
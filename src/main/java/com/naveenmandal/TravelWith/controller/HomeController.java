package com.naveenmandal.TravelWith.controller;

import com.naveenmandal.TravelWith.dto.ErrorResponse;
import com.naveenmandal.TravelWith.dto.JourneySearchRequest;
import com.naveenmandal.TravelWith.dto.JourneySearchResponse;
import com.naveenmandal.TravelWith.entity.StudentAccount;
import com.naveenmandal.TravelWith.repository.StationRepo;
import com.naveenmandal.TravelWith.service.PnrService;
import com.naveenmandal.TravelWith.service.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/travel")
@RequiredArgsConstructor
public class HomeController {

    private final PnrService pnrService;
    private final StationRepo stationRepo;
    private final MyUserDetailsService accountService;

    @GetMapping("/me")
    public ResponseEntity<?> me(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(new ErrorResponse("Authentication is required."));
        }

        StudentAccount acc = accountService.getByPhoneNo(principal.getName());
        if (acc == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(acc);
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(
            Principal principal,
            @RequestBody JourneySearchRequest request
    ) {
        if (principal == null) {
            return ResponseEntity.status(401).body(new ErrorResponse("Authentication is required."));
        }

        String sourceStation = normalize(request.sourceStation());
        String destinationStation = normalize(request.destinationStation());

        if (sourceStation.isBlank() || !stationRepo.existsByName(sourceStation)) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Please select a valid Source Station from suggestions."));
        }

        if (destinationStation.isBlank() || !stationRepo.existsByName(destinationStation)) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Please select a valid Destination Station from suggestions."));
        }

        LocalTime st = LocalTime.parse(request.stationTime());
        LocalTime dt = LocalTime.parse(request.destArrivalTime());

        LocalDate jd = LocalDate.parse(request.journeyDate());
        LocalDate jed = (request.journeyEndDate() == null || request.journeyEndDate().isBlank())
                ? jd
                : LocalDate.parse(request.journeyEndDate());

        if (jed.isBefore(jd)) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Journey end date cannot be before journey date."));
        }

        String phoneNo = principal.getName();
        StudentAccount acc = accountService.getByPhoneNo(phoneNo);
        if (acc == null) {
            return ResponseEntity.status(401).body(new ErrorResponse("Authenticated account was not found."));
        }

        JourneySearchResponse response = pnrService.searchAndSaveJourney(
                phoneNo,
                acc.getName(),
                sourceStation,
                destinationStation,
                st,
                dt,
                normalize(request.trainNo()),
                jd,
                jed
        );

        return ResponseEntity.ok(response);
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}

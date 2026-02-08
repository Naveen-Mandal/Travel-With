package com.naveenmandal.TravelWith.controller;

import com.naveenmandal.TravelWith.entity.StudentAccount;
import com.naveenmandal.TravelWith.repository.StationRepo;
import com.naveenmandal.TravelWith.service.PnrService;
import com.naveenmandal.TravelWith.service.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PnrService pnrService;
    private final StationRepo stationRepo;
    private final MyUserDetailsService accountService;

    @ModelAttribute
    public void addProfile(Model model, Principal principal) {
        if (principal == null) return;
        StudentAccount acc = accountService.getByPhoneNo(principal.getName());
        model.addAttribute("displayName", acc.getName());
        model.addAttribute("displayPhoneNo", acc.getPhoneNo());
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/search")
    public String search(
            Principal principal,
            @RequestParam String sourceStation,
            @RequestParam String destinationStation,
            @RequestParam String stationTime,
            @RequestParam String destArrivalTime,
            @RequestParam(required = false) String trainNo,
            @RequestParam String journeyDate,
            @RequestParam(required = false) String journeyEndDate,
            Model model
    ) {
        String phoneNo = principal.getName();
        StudentAccount acc = accountService.getByPhoneNo(phoneNo);

        String name = acc.getName();

        sourceStation = sourceStation == null ? "" : sourceStation.trim();
        destinationStation = destinationStation == null ? "" : destinationStation.trim();

        if (!stationRepo.existsByName(sourceStation)) {
            model.addAttribute("error", "Please select a valid Source Station from suggestions.");
            return "index";
        }

        if (!stationRepo.existsByName(destinationStation)) {
            model.addAttribute("error", "Please select a valid Destination Station from suggestions.");
            return "index";
        }

        LocalTime st = LocalTime.parse(stationTime);
        LocalTime dt = LocalTime.parse(destArrivalTime);

        LocalDate jd = LocalDate.parse(journeyDate);
        LocalDate jed = (journeyEndDate == null || journeyEndDate.isBlank())
                ? jd
                : LocalDate.parse(journeyEndDate);

        if (jed.isBefore(jd)) {
            model.addAttribute("error", "Journey end date cannot be before journey date.");
            return "index";
        }

        pnrService.saveJourney(phoneNo, name, sourceStation, destinationStation, st, dt, trainNo, jd, jed);

        var matches = pnrService.findMatchesAtSource(phoneNo, sourceStation, st, trainNo, jd, jed);
        var matchesAtDestination = pnrService.findMatchesAtDestination(phoneNo, destinationStation, dt, jed);

        model.addAttribute("matches", matches);
        model.addAttribute("matchesAtDestination", matchesAtDestination);

        model.addAttribute("trainNo", trainNo);
        model.addAttribute("journeyDate", jd);
        model.addAttribute("journeyEndDate", jed);
        model.addAttribute("sourceStation", sourceStation);
        model.addAttribute("destinationStation", destinationStation);
        model.addAttribute("stationTime", st);
        model.addAttribute("destArrivalTime", dt);

        return "result";
    }
}

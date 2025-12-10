package com.naveenmandal.TravelWith.controller;


import com.naveenmandal.TravelWith.entity.User;
import com.naveenmandal.TravelWith.service.PnrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private PnrService pnrService;


    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping(path = "/search")
    public String searchPnr(@RequestParam("phoneNo") String phoneNo,
                            @RequestParam("name") String name,
                            @RequestParam("sourceStation") String sourceStation,
                            @RequestParam("destinationStation") String destinationStation,
                            @RequestParam("stationTime") String stationTimeStr,
                            @RequestParam("trainNo") String trainNo,
                            @RequestParam("journeyDate") String journeyDateStr,
                            Model model) {

        // Parse time and date from simple strings (HH:mm, yyyy-MM-dd)
        LocalTime stationTime = LocalTime.parse(stationTimeStr);
        LocalDate journeyDate = LocalDate.parse(journeyDateStr);

        List<User> matches = pnrService.saveAndFindMatches(
                phoneNo,
                name,
                sourceStation,
                destinationStation,
                stationTime,
                trainNo,
                journeyDate
        );

        model.addAttribute("matches", matches);
        model.addAttribute("trainNo", trainNo);
        model.addAttribute("journeyDate", journeyDate);
        model.addAttribute("sourceStation", sourceStation);
        model.addAttribute("stationTime", stationTime);


        return "result";
    }
}
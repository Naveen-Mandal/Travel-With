package com.naveenmandal.TravelWith.controller;

import com.naveenmandal.TravelWith.service.StudentAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final StudentAccountService accountService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String phoneNo,
            @RequestParam String name,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model
    ) {
        try {
            // Validate that passwords match
            if (!password.equals(confirmPassword)) {
                model.addAttribute("error", "Passwords do not match. Please try again.");
                return "register";
            }
            
            accountService.register(name, phoneNo, password);
            return "redirect:/login?registered";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}

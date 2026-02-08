
package com.naveenmandal.TravelWith.controller;

import com.naveenmandal.TravelWith.security.JwtUtil;
import com.naveenmandal.TravelWith.service.MyUserDetailsService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final MyUserDetailsService accountService;


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username, @RequestParam String password, HttpServletResponse response, Model model) {
        try {
            // Use the helper method to handle login logic
            authenticateAndSetCookie(username, password, response);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", "Invalid phone number or password");
            return "login";
        }
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
            HttpServletResponse response, // Add response to set cookie
            Model model
    ) {
        try {
            if (!password.equals(confirmPassword)) {
                model.addAttribute("error", "Passwords do not match.");
                return "register";
            }

            // 1. Create the user in the database
            accountService.register(name, phoneNo, password);

            // 2. AUTO-LOGIN: Authenticate and set cookie immediately
            authenticateAndSetCookie(phoneNo, password, response);

            // 3. Redirect directly to home, skipping the login page
            return "redirect:/";

        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("accessToken", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/login?logout";
    }

    // --- Best Practice: Helper Method to avoid Code Duplication ---
    private void authenticateAndSetCookie(String username, String password, HttpServletResponse response) {
        // 1. Authenticate
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        // 2. Generate Token
        String token = jwtUtil.generateToken(username);

        // 3. Set Cookie
        Cookie cookie = new Cookie("accessToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // Keep true for Aiven/Production
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24* 20); // 10 Hours
        response.addCookie(cookie);
    }
}
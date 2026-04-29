
package com.naveenmandal.TravelWith.controller;

import com.naveenmandal.TravelWith.dto.AuthResponse;
import com.naveenmandal.TravelWith.dto.ErrorResponse;
import com.naveenmandal.TravelWith.dto.LoginRequest;
import com.naveenmandal.TravelWith.dto.RegisterRequest;
import com.naveenmandal.TravelWith.entity.StudentAccount;
import com.naveenmandal.TravelWith.security.JwtUtil;
import com.naveenmandal.TravelWith.service.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final MyUserDetailsService accountService;


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
        try {
            String phoneNo = request.phoneNo();
            authenticate(phoneNo, request.password());
            return ResponseEntity.ok(buildAuthResponse(phoneNo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Invalid phone number or password"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            if (!request.password().equals(request.confirmPassword())) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Passwords do not match."));
            }

            accountService.register(request.name(), request.phoneNo(), request.password());
            authenticate(request.phoneNo(), request.password());
            return ResponseEntity.status(HttpStatus.CREATED).body(buildAuthResponse(request.phoneNo()));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.noContent().build();
    }

    private void authenticate(String phoneNo, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(phoneNo, password));
    }

    private AuthResponse buildAuthResponse(String phoneNo) {
        StudentAccount account = accountService.getByPhoneNo(phoneNo);

        return new AuthResponse(
                jwtUtil.generateToken(phoneNo),
                "Bearer",
                phoneNo,
                account == null ? null : account.getName()
        );
    }
}

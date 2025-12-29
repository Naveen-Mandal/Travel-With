package com.naveenmandal.TravelWith.service;

import com.naveenmandal.TravelWith.entity.StudentAccount;
import com.naveenmandal.TravelWith.repository.StudentAccountRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentAccountService implements UserDetailsService {

    private final StudentAccountRepo repo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String phoneNo) throws UsernameNotFoundException {
        String normalized = normalizePhone(phoneNo);
        StudentAccount acc = repo.findById(normalized)
                .orElseThrow(() -> new UsernameNotFoundException("No account for phone: " + normalized));

        return org.springframework.security.core.userdetails.User
                .withUsername(acc.getPhoneNo())
                .password(acc.getPasswordHash())
                .roles("STUDENT")
                .disabled(!acc.isEnabled())
                .build();
    }

    public StudentAccount getByPhoneNo(String phoneNo) {
        String normalized = normalizePhone(phoneNo);
        return repo.findById(normalized)
                .orElseThrow(() -> new RuntimeException("No account found for phone: " + normalized));
    }

    public void register(String name, String phoneNo, String password) {
        String normalized = normalizePhone(phoneNo);
        
        if (repo.existsById(normalized)) {
            throw new RuntimeException("Account with this phone number already exists");
        }

        String passwordHash = passwordEncoder.encode(password);
        StudentAccount account = new StudentAccount(normalized, name, passwordHash, true);
        repo.save(account);
    }

    private String normalizePhone(String phoneNo) {
        return (phoneNo == null) ? "" : phoneNo.trim();
    }
}

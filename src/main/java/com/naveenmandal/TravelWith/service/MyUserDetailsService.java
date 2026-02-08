package com.naveenmandal.TravelWith.service;

import com.naveenmandal.TravelWith.entity.StudentAccount;
import com.naveenmandal.TravelWith.entity.UserPrincipal;
import com.naveenmandal.TravelWith.repository.StudentAccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy; // Use Lazy to avoid circular dependency
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private StudentAccountRepo repo;

    @Autowired
    @Lazy // Important: Breaks the cycle between SecurityConfig -> Service -> SecurityConfig
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String phoneNo) throws UsernameNotFoundException {
        StudentAccount student = repo.findByPhoneNo(phoneNo);
        if (student == null) {
            throw new UsernameNotFoundException("User not found with phone number " + phoneNo);
        }
        return new UserPrincipal(student);
    }

    // --- ADD THIS MISSING METHOD ---
    public void register(String name, String phoneNo, String rawPassword) {
        // 1. Check if user already exists
        if (repo.findByPhoneNo(phoneNo) != null) {
            throw new RuntimeException("User already exists with this phone number.");
        }

        // 2. Create and Save User
        StudentAccount newAccount = new StudentAccount();
        newAccount.setName(name);
        newAccount.setPhoneNo(phoneNo);
        // 3. ENCODE THE PASSWORD (Critical!)
        newAccount.setPasswordHash(passwordEncoder.encode(rawPassword));
        newAccount.setEnabled(true);

        repo.save(newAccount);
    }

    public StudentAccount getByPhoneNo(String phoneNo) {
        return repo.findByPhoneNo(phoneNo);
    }
}
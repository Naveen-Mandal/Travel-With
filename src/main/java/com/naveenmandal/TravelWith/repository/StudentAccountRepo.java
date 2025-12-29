package com.naveenmandal.TravelWith.repository;

import com.naveenmandal.TravelWith.entity.StudentAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentAccountRepo extends JpaRepository<StudentAccount, String> {
    Optional<StudentAccount> findByPhoneNo(String phoneNo); // if phone isn't @Id
}


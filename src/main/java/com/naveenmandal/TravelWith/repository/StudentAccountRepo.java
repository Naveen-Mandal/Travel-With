package com.naveenmandal.TravelWith.repository;

import com.naveenmandal.TravelWith.entity.StudentAccount;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StudentAccountRepo extends JpaRepository<StudentAccount, String> {
    StudentAccount findByPhoneNo(String phoneNo); // if phone isn't @Id
}


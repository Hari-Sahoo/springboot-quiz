package com.example.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.models.EmpExam;

public interface EmpxamRepository extends JpaRepository<EmpExam, Integer> {

    Optional<EmpExam> findByEmail(String email);

    boolean existsByEmail(String email);

}

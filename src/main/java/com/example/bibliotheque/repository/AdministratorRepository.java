package com.example.bibliotheque.repository;

import com.example.bibliotheque.models.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface AdministratorRepository extends JpaRepository<Administrator,Long> {
    Optional<Administrator> findByEmployeeId(String employeeId);
}

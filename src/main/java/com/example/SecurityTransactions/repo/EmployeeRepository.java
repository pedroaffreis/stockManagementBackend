package com.example.SecurityTransactions.repo;

import com.example.SecurityTransactions.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    Optional<Employee> findEmployeeById(Long id);

    Boolean existsByEmail(String email);

    Boolean existsByPersonalCode(Long personalCode);

}

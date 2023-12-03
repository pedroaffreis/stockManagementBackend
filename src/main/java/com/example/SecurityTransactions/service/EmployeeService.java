package com.example.SecurityTransactions.service;

import com.example.SecurityTransactions.entity.Employee;
import com.example.SecurityTransactions.exception.DuplicateEntryException;
import com.example.SecurityTransactions.exception.EmployeeNotFoundException;
import com.example.SecurityTransactions.repo.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee addEmployee(Employee employee) {
        if (employeeRepository.existsByPersonalCode(employee.getPersonalCode())) {
            throw new DuplicateEntryException("Employee with personal code already exists");
        } else if (employeeRepository.existsByEmail(employee.getEmail().toLowerCase())) {
            throw new DuplicateEntryException("Employee with email already exists");
        }
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        return employeeRepository.save(employee);
    }
    public Employee findEmployeeById(Long id) {
        return employeeRepository.findEmployeeById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee by ID " + id + " was not found."));
    }

    public Employee updateEmployee(Employee employee) {

        String currentEmail = employeeRepository.findEmployeeById(employee.getId()).get().getEmail();
        String currentPassword = employeeRepository.findEmployeeById(employee.getId()).get().getPassword();

      if (!currentEmail.equals(employee.getEmail().toLowerCase()) && employeeRepository.existsByEmail(employee.getEmail())){
            throw new DuplicateEntryException("Employee with email already exists");
        }
        if (!currentPassword.equals(employee.getPassword())) {
            employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        }
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    public Employee findEByTransactionId(Long transId) {
        List<Employee> employees = employeeRepository.findAll();
        Optional<Employee> optionalEmployee = employees.stream()
                .filter(emp -> emp.getTransaction().stream()
                        .anyMatch(transaction -> transaction.getId().equals(transId)))
                .findFirst();
        return optionalEmployee.orElse(null);
    }
}

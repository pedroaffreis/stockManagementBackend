package com.example.SecurityTransactions.repo;

import com.example.SecurityTransactions.entity.Employee;
import com.example.SecurityTransactions.entity.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class EmployeeRepositoryTest {

    @Autowired
    public EmployeeRepository employeeRepository;

    @AfterEach
    void tearDown() {
        employeeRepository.deleteAll();
    }

    @Test
    public void canFindByEmail() {
        //Given
        String email = "pedro@gmail.com";
        Employee testEmployee = new Employee(1L, "FirstName",
                "LastName", "root", 11111L,
                email, "address", "1111", Role.ROLE_USER,
                new ArrayList<>());

        //Saving the employee to the database
        employeeRepository.save(testEmployee);

        //When
        String employeeEmail = testEmployee.getEmail();
        Optional<Employee> foundEmployee = employeeRepository.findByEmail(employeeEmail);

        //Then
        assertTrue(foundEmployee.isPresent());
        assertEquals(testEmployee.getEmail(), foundEmployee.get().getEmail());
    }

    @Test
    public void itShouldCheckWhenEmployeeEmailExists() {
        //Given
        String email = "pedro@gmail.com";
        Employee testEmployee = new Employee(1L, "FirstName",
                "LastName", "root", 11111L,
                email, "address", "1111", Role.ROLE_USER,
                new ArrayList<>());

        //Saving the employee to the database
        employeeRepository.save(testEmployee);

        //When
        boolean expected = employeeRepository.existsByEmail(email);

        //Then
        assertThat(expected).isTrue();
    }

    @Test
    public void canFindEmployeeById() {
        //Given
        Long id = 1L;
        Employee testEmployee = new Employee(id, "FirstName",
                "LastName", "root", 11111L,
                "example@example1.com", "address", "1111",
                Role.ROLE_USER, new ArrayList<>());

        //Saving the employee to the database
        employeeRepository.save(testEmployee);

        //When
        Optional<Employee> result = employeeRepository.findEmployeeById(id);

        // Then
        assertThat(result.isPresent());
        assertThat(result.get().getId()).isEqualTo(id);
    }


}
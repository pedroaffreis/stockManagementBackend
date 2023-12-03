package com.example.SecurityTransactions.service;

import com.example.SecurityTransactions.entity.Employee;
import com.example.SecurityTransactions.entity.Role;
import com.example.SecurityTransactions.repo.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private EmployeeService underTest;

    @BeforeEach
    void setup() {
        underTest = new EmployeeService(employeeRepository, passwordEncoder);
    }


    @Test
    void canGetAllEmployees() {
        underTest.findAllEmployees();
        verify(employeeRepository).findAll();
    }


    @Test
    void canAddEmployee() {
        //Given
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Pedro");
        employee.setLastName("Reis");
        employee.setPassword("root");
        employee.setPersonalCode(123456789);
        employee.setEmail("pedrocarepas@gmail.com");
        employee.setAddress("Ravi tee 11");
        employee.setPhone("95564523");
        employee.setRole(Role.ROLE_ADMIN);
        when(passwordEncoder.encode("root")).thenReturn("encodedPassword");

        //When
        underTest.addEmployee(employee);
        ArgumentCaptor<Employee> employeeArgumentCaptor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(employeeArgumentCaptor.capture());
        Employee capturedEmployee = employeeArgumentCaptor.getValue();

        //Then
        assertThat(capturedEmployee).isEqualTo(employee);
        assertThat(capturedEmployee.getPassword()).isEqualTo("encodedPassword");
    }

    @Test
    void EmployeeService_UpdateEmployee() {
        //Given
        Employee testEmployee = Employee.builder()
                .id(1L)
                .firstName("FirstName")
                .lastName("LastName")
                .password("root")
                .personalCode(11111L)
                .email("example@example1.com")
                .address("address")
                .phone("1111")
                .role(Role.ROLE_USER)
                .transaction(new ArrayList<>())
                .build();

        // Mock the save method of the employee repository
        when(employeeRepository.save(testEmployee)).thenReturn(testEmployee);
        when(employeeRepository.findEmployeeById(testEmployee.getId())).thenReturn(Optional.of(testEmployee));
        // When
        Employee result = underTest.updateEmployee(testEmployee);

        // Then
        verify(employeeRepository, times(1)).save(testEmployee);
        assertEquals(testEmployee, result);
    }

    @Test
    void canDeleteEmployee() {
        //Given
        Long id = 10L;
        Employee testEmployee = Employee.builder()
                .id(id)
                .firstName("FirstName")
                .lastName("LastName")
                .password("root")
                .personalCode(11111L)
                .email("example@example1.com")
                .address("address")
                .phone("1111")
                .role(Role.ROLE_USER)
                .transaction(new ArrayList<>())
                .build();

        //Saving the employee to the database
        employeeRepository.save(testEmployee);

        //When
        underTest.deleteEmployee(id);

        // Then
        verify(employeeRepository).deleteById(id);
    }

}

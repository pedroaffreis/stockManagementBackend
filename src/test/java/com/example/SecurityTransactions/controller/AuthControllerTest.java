package com.example.SecurityTransactions.controller;

import com.example.SecurityTransactions.dto.LoginDto;
import com.example.SecurityTransactions.entity.Employee;
import com.example.SecurityTransactions.entity.Role;
import com.example.SecurityTransactions.repo.EmployeeRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@SpringBootTest
public class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    private String secretKey = "FQhSqsuexCU=";

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(authController, "secretKey", secretKey);
    }

    @Test
    public void testLogin() {
        // Arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("password");

        Employee testEmployee = new Employee();
        testEmployee.setEmail("test@example.com");
        testEmployee.setPassword("password");
        testEmployee.setRole(Role.ROLE_USER);
        when(employeeRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(testEmployee));
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        ResponseEntity<String> responseEntity = authController.login(loginDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        String jwtToken = responseEntity.getBody();

        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
        assertNotNull(claims);
    }
}

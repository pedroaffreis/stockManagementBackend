package com.example.SecurityTransactions.repo;

import com.example.SecurityTransactions.entity.*;
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

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository underTest;
    @Autowired
    private ShareRepository shareRepository;
    @Autowired
    private EmployeeRepository employeeRepository;


    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    public void testFindTransactionById() {

        //Given

        Share testShare = new Share(1L, "CompanyName", "ShareName", "AAPL", "US", "Technology", "USD", new ArrayList<>());
        shareRepository.save(testShare);

        Employee testEmployee = new Employee(1L, "FirstName", "LastName", "root", 11111L, "example@example1.com", "address", "1111", Role.ROLE_USER, new ArrayList<>());
        employeeRepository.save(testEmployee);

        Long transactionId = 1L;
        Transaction transaction = new Transaction();
        transaction.setId(transactionId);
        transaction.setEmployee(testEmployee);
        transaction.setShare(testShare);
        transaction.setFx(1.23f);
        transaction.setPrice(100);
        transaction.setVolume(100l);
        transaction.setType(TransactionType.PURCHASE);


        underTest.save(transaction);

        // When
        Optional<Transaction> result = underTest.findTransactionById(transactionId);

        // Then
        assertThat(result.isPresent());
        assertThat(result.get().getId()).isEqualTo(transactionId);
    }

}

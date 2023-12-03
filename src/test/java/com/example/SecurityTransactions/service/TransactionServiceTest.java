package com.example.SecurityTransactions.service;

import com.example.SecurityTransactions.entity.*;
import com.example.SecurityTransactions.exception.ShortSellingNotAllowedException;
import com.example.SecurityTransactions.exception.TransactionNotFoundException;
import com.example.SecurityTransactions.repo.EmployeeRepository;
import com.example.SecurityTransactions.repo.ShareRepository;
import com.example.SecurityTransactions.repo.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ShareRepository shareRepository;
    @InjectMocks
    private TransactionService underTest;

    @BeforeEach
    void setup() {
        underTest = new TransactionService(transactionRepository, employeeRepository, shareRepository);
    }

    @Test
    void canFindAllTransactions() {
        underTest.findAllTransactions();
        verify(transactionRepository).findAll();
    }

    @Test
    void testFindTransactionByIdTransactionDoesNotExist() {
        long id = 1L;
        assertThatThrownBy(() -> underTest.findTransactionById(id))
                .isInstanceOf(TransactionNotFoundException.class);
    }

    @Nested
    class CheckBalance {
        @Test
        void testGetShareBalanceOnlyPurchase() {
            // Given
            String symbol = "AAPL";
            Share testShare = new Share(1L, "CompanyName", "ShareName", symbol, "US", "Technology", "USD", new ArrayList<>());
            List<Transaction> transactions = new ArrayList<>();
            transactions.add(new Transaction(1L, TransactionType.PURCHASE, testShare, 100L, 100.23f, 1.23f, new Employee(1L, "FirstName", "LastName", "root", 11111L, "example@example1.com", "address", "1111", Role.ROLE_USER, new ArrayList<>())));
            testShare.setStockTransactions(transactions);
            when(shareRepository.findBySymbol(symbol)).thenReturn(Optional.of(testShare));

            // When
            long shareBalance = underTest.shareBalance(symbol);

            // Then
            assertThat(shareBalance).isEqualTo(100L);
        }

        @Test
        void testGetShareBalancePurchaseAndSale() {
            // Given
            String symbol = "AAPL";
            Share testShare = new Share(1L, "CompanyName", "ShareName", symbol, "US", "Technology", "USD", new ArrayList<>());
            Employee testEmployee = new Employee(1L, "FirstName", "LastName", "root", 11111L, "example@example1.com", "address", "1111", Role.ROLE_USER, new ArrayList<>());
            List<Transaction> transactions = new ArrayList<>();
            transactions.add(new Transaction(1L, TransactionType.PURCHASE, testShare, 100L, 100.23f, 1.23f, testEmployee));
            transactions.add(new Transaction(1L, TransactionType.SALE, testShare, 50L, 100.23f, 1.23f, testEmployee));
            testShare.setStockTransactions(transactions);
            when(shareRepository.findBySymbol(symbol)).thenReturn(Optional.of(testShare));

            // When
            long shareBalance = underTest.shareBalance(symbol);

            // Then
            assertThat(shareBalance).isEqualTo(50L);
        }
    }

    @Nested
    class AddTransactionTests {
        @Test
        void testAddTransactionPurchase() {
            // Given
            Long empId = 1L;
            String symbol = "AAPL";

            Employee testEmployee = new Employee(empId, "FirstName", "LastName", "root", 11111L, "example@example1.com", "address", "1111", Role.ROLE_USER, new ArrayList<>());
            when(employeeRepository.findById(empId)).thenReturn(Optional.of(testEmployee));

            Share testShare = new Share(1L, "CompanyName", "ShareName", "AAPL", "US", "Technology", "USD", new ArrayList<>());
            when(shareRepository.findBySymbol(symbol)).thenReturn(Optional.of(testShare));

            Transaction transaction = new Transaction();
            transaction.setType(TransactionType.PURCHASE);

            // When
            underTest.addTransaction(transaction, empId, symbol);

            // Then
            verify(transactionRepository, times(1)).save(transaction);
        }

        @Test
        void testAddTransactionSaleInsufficientShares() {
            // Given
            Long empId = 1L;
            String symbol = "AAPL";

            Employee testEmployee = new Employee(empId, "FirstName", "LastName", "root", 11111L, "example@example1.com", "address", "1111", Role.ROLE_USER, new ArrayList<>());

            Share testShare = new Share(1L, "CompanyName", "ShareName", symbol, "US", "Technology", "USD", new ArrayList<>());
            when(shareRepository.findBySymbol(symbol)).thenReturn(Optional.of(testShare));

            Transaction transaction = new Transaction(1L, TransactionType.SALE, testShare, 50L, 100.23f, 1.23f, testEmployee);

            // WhenThen
            assertThrows(ShortSellingNotAllowedException.class,
                    () -> underTest.addTransaction(transaction, empId, symbol));

            verify(transactionRepository, never()).save(transaction);
        }
    }

    @Nested
    class UpdateTransaction {
        Long empId = 1L;
        String symbol = "AAPL";
        Share testShare = new Share(1L, "CompanyName", "ShareName", symbol, "US", "Technology", "USD", new ArrayList<>());
        Employee testEmployee = new Employee(empId, "FirstName", "LastName", "root", 11111L, "example@example1.com", "address", "1111", Role.ROLE_USER, new ArrayList<>());
        Transaction transaction = new Transaction(1L, TransactionType.PURCHASE, testShare, 50L, 100.23f, 1.23f, testEmployee);
        Transaction updatedTransaction = new Transaction(1L, TransactionType.PURCHASE, testShare, 100L, 100.23f, 1.23f, testEmployee);

        @Test
        public void updateTransactionSuccess() {

            // Given
            when(transactionRepository.findTransactionById(transaction.getId())).thenReturn(Optional.of(transaction));
            when(shareRepository.findBySymbol(symbol)).thenReturn(Optional.of(testShare));
            when(transactionRepository.save(updatedTransaction)).thenReturn(updatedTransaction);

            // When
            Transaction result = underTest.updateTransaction(updatedTransaction);

            // Then
            verify(transactionRepository, times(1)).save(updatedTransaction);
            assertEquals(updatedTransaction, result);

        }

        @Test
        public void updateTransactionInsufficientAmount() {
            //Given
            when(transactionRepository.findTransactionById(transaction.getId())).thenReturn(Optional.of(transaction));
            when(shareRepository.findBySymbol(symbol)).thenReturn(Optional.of(testShare));

            Transaction updateToSaleTransaction = new Transaction(1L, TransactionType.SALE, testShare, 10000L, 100.23f, 1.23f, testEmployee);

            //When Then
            assertThrows(ShortSellingNotAllowedException.class,
                    () -> underTest.updateTransaction(updateToSaleTransaction));

            verify(transactionRepository, never()).save(updateToSaleTransaction);
        }
    }

    @Nested
    class DeleteTransactionTests {
        Long empId = 1L;
        String symbol = "AAPL";
        Share testShare = new Share(1L, "CompanyName", "ShareName", symbol, "US", "Technology", "USD", new ArrayList<>());
        Employee testEmployee = new Employee(empId, "FirstName", "LastName", "root", 11111L, "example@example1.com", "address", "1111", Role.ROLE_USER, new ArrayList<>());
        Transaction transaction = new Transaction(1L, TransactionType.PURCHASE, testShare, 50L, 100.23f, 1.23f, testEmployee);

        @Test
        void canDeleteTransaction() {
            testShare.getStockTransactions().add(transaction);
            Long id = 1L;
            // Mock the share balance to be equal to the transaction volume
            when(transactionRepository.findTransactionById(id)).thenReturn(Optional.of(transaction));
            when(shareRepository.findBySymbol(symbol)).thenReturn(Optional.of(testShare));

            // When
            underTest.deleteTransaction(transaction.getId());

            // Then
            verify(transactionRepository).deleteById(transaction.getId());


        }

        @Test
        void willThrowExceptionWhenDeleteTransactionResultsInInsufficientAmount() {

            //Given
            long id = 1;
            when(transactionRepository.findTransactionById(transaction.getId())).thenReturn(Optional.of(transaction));
            when(shareRepository.findBySymbol(symbol)).thenReturn(Optional.of(testShare));

            //When Then
            assertThatThrownBy(() -> underTest.deleteTransaction(id))
                    .isInstanceOf(ShortSellingNotAllowedException.class);
            verify(transactionRepository, never()).deleteById(any());
        }

    }
}


package com.example.SecurityTransactions.controller;


import com.example.SecurityTransactions.entity.Transaction;
import com.example.SecurityTransactions.exception.DuplicateEntryException;
import com.example.SecurityTransactions.exception.ShortSellingNotAllowedException;
import com.example.SecurityTransactions.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.findAllTransactions();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addTransaction(@RequestBody Transaction transaction, @RequestParam Long empId, @RequestParam String secId) {
        try {
            Transaction addedTransaction = transactionService.addTransaction(transaction, empId, secId);
            return new ResponseEntity<>(addedTransaction, HttpStatus.CREATED);
        } catch (ShortSellingNotAllowedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateTransaction(@RequestBody Transaction transaction) {
        try{
            Transaction updatedTransaction = transactionService.updateTransaction(transaction);
            return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
        }catch (ShortSellingNotAllowedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable("id") Long id) {
        Transaction foundTransaction = transactionService.findTransactionById(id);
        return new ResponseEntity<>(foundTransaction, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTransactionById(@PathVariable("id") Long id) {
        try{
            transactionService.deleteTransaction(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ShortSellingNotAllowedException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
}

package com.example.SecurityTransactions.exception;

public class ShortSellingNotAllowedException extends RuntimeException {
    public ShortSellingNotAllowedException(String msg) {
        super(msg);
    }
}

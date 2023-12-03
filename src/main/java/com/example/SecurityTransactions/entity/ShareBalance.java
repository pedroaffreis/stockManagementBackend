package com.example.SecurityTransactions.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShareBalance {

    private String symbol;

    private String shareName;

    private long balance;

    private String currency;

    private float bookPrice;
}

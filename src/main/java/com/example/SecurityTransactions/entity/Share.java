package com.example.SecurityTransactions.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Share implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "share_name", nullable = false)
    private String shareName;
    @Column(name = "symbol", nullable = false, unique = true)
    private String symbol;
    @Column(name = "country", nullable = false)
    private String country;
    @Column(name = "economic_field", nullable = false)
    private String economicField;

    @Column(name = "currency", nullable = false)
    private String currency;

    @JsonManagedReference("share")
    @OneToMany(mappedBy = "share", fetch = FetchType.LAZY)
    private List<Transaction> stockTransactions = new ArrayList<>();

}

package com.example.SecurityTransactions.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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

public class Employee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "personal_code", nullable = false, unique = true)
    private long personalCode;

    @Email
    @Column(nullable = false, unique = true)
    private String email;
    @Column(name = "address", nullable = false)
    private String address;
    @Column(name = "phone_number", nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonManagedReference("employee")
    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    List<Transaction> transaction = new ArrayList<>();


}
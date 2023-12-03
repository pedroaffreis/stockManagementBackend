package com.example.SecurityTransactions.dto;

import com.example.SecurityTransactions.entity.Role;
import lombok.Data;

@Data
public class LoginDto {
    private String email;
    private String password;
    private Role role;
}

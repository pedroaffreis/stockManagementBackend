package com.example.SecurityTransactions.security;

import com.example.SecurityTransactions.entity.Employee;
import com.example.SecurityTransactions.entity.Role;
import com.example.SecurityTransactions.exception.EmployeeNotFoundException;
import com.example.SecurityTransactions.repo.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private EmployeeRepository employeeRepository;

    @Autowired
    public CustomUserDetailsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee emp = employeeRepository.findByEmail(email).orElseThrow(() -> new EmployeeNotFoundException("Employee with e-mail:" + email + " does not exist"));
        return new User(emp.getEmail(), emp.getPassword(), List.of(mapRolesToAuthorities(emp.getRole())));
    }

    private GrantedAuthority mapRolesToAuthorities(Role role) {
        return new SimpleGrantedAuthority(role.toString());
    }
}

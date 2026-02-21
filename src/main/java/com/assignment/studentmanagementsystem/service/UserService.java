package com.assignment.studentmanagementsystem.service;

import com.assignment.studentmanagementsystem.repository.ManagementRepository;
import com.assignment.studentmanagementsystem.security.UserAccount;
import com.assignment.studentmanagementsystem.security.UserAccount.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final ManagementRepository managementRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
        ManagementRepository managementRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.managementRepository = managementRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserAccount registerUser(
        String username,
        String password,
        Role role
    ) {
        if (managementRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        UserAccount user = new UserAccount();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);

        return managementRepository.saveUser(user);
    }
}

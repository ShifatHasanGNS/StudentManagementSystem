package com.assignment.studentmanagementsystem.service;

import com.assignment.studentmanagementsystem.security.UserAccount;
import com.assignment.studentmanagementsystem.repository.ManagementRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final ManagementRepository managementRepository;

    public UserDetailsServiceImpl(ManagementRepository managementRepository) {
        this.managementRepository = managementRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
        throws UsernameNotFoundException {
        UserAccount user = managementRepository
            .findUserByUsername(username)
            .orElseThrow(() ->
                new UsernameNotFoundException("User not found: " + username)
            );

        return user;
    }
}

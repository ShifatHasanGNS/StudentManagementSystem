package com.assignment.studentmanagementsystem.service;

import com.assignment.studentmanagementsystem.repository.ManagementRepository;
import com.assignment.studentmanagementsystem.security.UserAccount;
import com.assignment.studentmanagementsystem.security.UserAccount.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// File path: src/test/java/com/assignment/studentmanagementsystem/service/UserServiceTest.java

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private ManagementRepository managementRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_success_savesEncodedPassword() {
        when(managementRepository.existsByUsername("newuser@test.com")).thenReturn(false);
        when(passwordEncoder.encode("pass123")).thenReturn("encoded_pass");
        when(managementRepository.saveUser(any(UserAccount.class))).thenAnswer(i -> i.getArgument(0));

        UserAccount result = userService.registerUser("newuser@test.com", "pass123", Role.STUDENT);

        assertThat(result.getUsername()).isEqualTo("newuser@test.com");
        assertThat(result.getPassword()).isEqualTo("encoded_pass");
        assertThat(result.getRole()).isEqualTo(Role.STUDENT);
        verify(managementRepository).saveUser(any(UserAccount.class));
    }

    @Test
    void registerUser_duplicateUsername_throwsException() {
        when(managementRepository.existsByUsername("existing@test.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.registerUser("existing@test.com", "pass", Role.STUDENT))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Username already exists");

        verify(managementRepository, never()).saveUser(any());
    }

    @Test
    void registerUser_teacherRole_setsCorrectRole() {
        when(managementRepository.existsByUsername("teacher@test.com")).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(managementRepository.saveUser(any())).thenAnswer(i -> i.getArgument(0));

        UserAccount result = userService.registerUser("teacher@test.com", "pass", Role.TEACHER);

        assertThat(result.getRole()).isEqualTo(Role.TEACHER);
    }
}

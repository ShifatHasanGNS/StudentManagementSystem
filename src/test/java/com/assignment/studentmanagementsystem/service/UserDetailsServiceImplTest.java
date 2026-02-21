package com.assignment.studentmanagementsystem.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.assignment.studentmanagementsystem.repository.ManagementRepository;
import com.assignment.studentmanagementsystem.security.UserAccount;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private ManagementRepository managementRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void loadUserByUsername_found_returnsUserDetails() {
        UserAccount user = new UserAccount(
            "student@test.com",
            "encoded",
            UserAccount.Role.STUDENT
        );
        when(
            managementRepository.findUserByUsername("student@test.com")
        ).thenReturn(Optional.of(user));

        UserDetails result = userDetailsService.loadUserByUsername(
            "student@test.com"
        );

        assertThat(result.getUsername()).isEqualTo("student@test.com");
        assertThat(result.getAuthorities()).anyMatch(a ->
            a.getAuthority().equals("ROLE_STUDENT")
        );
    }

    @Test
    void loadUserByUsername_notFound_throwsUsernameNotFoundException() {
        when(
            managementRepository.findUserByUsername("ghost@test.com")
        ).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            userDetailsService.loadUserByUsername("ghost@test.com")
        )
            .isInstanceOf(UsernameNotFoundException.class)
            .hasMessageContaining("User not found");
    }

    @Test
    void loadUserByUsername_teacher_hasTeacherRole() {
        UserAccount user = new UserAccount(
            "teacher@test.com",
            "encoded",
            UserAccount.Role.TEACHER
        );
        when(
            managementRepository.findUserByUsername("teacher@test.com")
        ).thenReturn(Optional.of(user));

        UserDetails result = userDetailsService.loadUserByUsername(
            "teacher@test.com"
        );

        assertThat(result.getAuthorities()).anyMatch(a ->
            a.getAuthority().equals("ROLE_TEACHER")
        );
    }
}

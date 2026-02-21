package com.assignment.studentmanagementsystem;

import com.assignment.studentmanagementsystem.security.UserAccount;
import com.assignment.studentmanagementsystem.service.UserService;
import com.assignment.studentmanagementsystem.repository.ManagementRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("dev")
@SpringBootTest
public class RegistrationServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private ManagementRepository managementRepository;

    @Test
    void registerUser_savesUser() {
        String username = "svc.user@test.com";

        userService.registerUser(username, "password123", UserAccount.Role.STUDENT);

        assertThat(managementRepository.existsByUsername(username)).isTrue();
    }
}

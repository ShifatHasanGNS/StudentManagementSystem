package com.assignment.studentmanagementsystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.assignment.studentmanagementsystem.security.UserAccount.Role;
import com.assignment.studentmanagementsystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
class RegistrationControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(springSecurity())
            .build();
    }

    @Test
    void getRegisterPage_returnsChooseRoleView() throws Exception {
        mockMvc
            .perform(get("/register"))
            .andExpect(status().isOk())
            .andExpect(view().name("register/choose-role"));
    }

    @Test
    void getStudentRegistrationPage_returnsStudentView() throws Exception {
        mockMvc
            .perform(get("/register/student"))
            .andExpect(status().isOk())
            .andExpect(view().name("register/student"));
    }

    @Test
    void getTeacherRegistrationPage_returnsTeacherView() throws Exception {
        mockMvc
            .perform(get("/register/teacher"))
            .andExpect(status().isOk())
            .andExpect(view().name("register/teacher"));
    }

    @Test
    void postRegisterStudent_success_redirectsToLogin() throws Exception {
        when(
            userService.registerUser(
                eq("newstudent@test.com"),
                eq("pass123"),
                eq(Role.STUDENT)
            )
        ).thenReturn(null);

        mockMvc
            .perform(
                post("/register/student")
                    .param("username", "newstudent@test.com")
                    .param("password", "pass123")
                    .with(csrf())
            )
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login?registered"));
    }

    @Test
    void postRegisterTeacher_success_redirectsToLogin() throws Exception {
        when(
            userService.registerUser(
                eq("newteacher@test.com"),
                eq("pass123"),
                eq(Role.TEACHER)
            )
        ).thenReturn(null);

        mockMvc
            .perform(
                post("/register/teacher")
                    .param("username", "newteacher@test.com")
                    .param("password", "pass123")
                    .with(csrf())
            )
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login?registered"));
    }

    @Test
    void postRegisterStudent_duplicateUsername_returnsFormWithError()
        throws Exception {
        when(
            userService.registerUser(any(), any(), eq(Role.STUDENT))
        ).thenThrow(new RuntimeException("Username already exists"));

        mockMvc
            .perform(
                post("/register/student")
                    .param("username", "duplicate@test.com")
                    .param("password", "pass123")
                    .with(csrf())
            )
            .andExpect(status().isOk())
            .andExpect(view().name("register/student"))
            .andExpect(model().attributeExists("error"));
    }

    @Test
    void postRegisterTeacher_duplicateUsername_returnsFormWithError()
        throws Exception {
        when(
            userService.registerUser(any(), any(), eq(Role.TEACHER))
        ).thenThrow(new RuntimeException("Username already exists"));

        mockMvc
            .perform(
                post("/register/teacher")
                    .param("username", "duplicate@test.com")
                    .param("password", "pass123")
                    .with(csrf())
            )
            .andExpect(status().isOk())
            .andExpect(view().name("register/teacher"))
            .andExpect(model().attributeExists("error"));
    }

    @Test
    void getRegisterProfileLegacy_redirectsToProfile() throws Exception {
        mockMvc
            .perform(get("/register/profile"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/profile"));
    }
}

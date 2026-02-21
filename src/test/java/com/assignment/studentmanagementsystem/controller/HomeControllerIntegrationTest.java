package com.assignment.studentmanagementsystem.controller;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.assignment.studentmanagementsystem.model.Student;
import com.assignment.studentmanagementsystem.model.Teacher;
import com.assignment.studentmanagementsystem.repository.ManagementRepository;
import com.assignment.studentmanagementsystem.security.UserAccount;
import com.assignment.studentmanagementsystem.security.UserAccount.Role;
import com.assignment.studentmanagementsystem.service.ManagementService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
class HomeControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private ManagementRepository managementRepository;

    @MockitoBean
    private ManagementService managementService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(springSecurity())
            .build();
    }

    @Test
    void rootPath_redirectsToLogin() throws Exception {
        mockMvc
            .perform(get("/"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login"));
    }

    @Test
    void loginPage_returnsOk() throws Exception {
        mockMvc
            .perform(get("/login"))
            .andExpect(status().isOk())
            .andExpect(view().name("login"));
    }

    @Test
    void healthEndpoint_returnsOk() throws Exception {
        mockMvc
            .perform(get("/health"))
            .andExpect(status().isOk())
            .andExpect(content().string("OK"));
    }

    @Test
    void dashboard_unauthenticated_redirectsToLogin() throws Exception {
        mockMvc
            .perform(get("/dashboard"))
            .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "student@test.com", roles = "STUDENT")
    void dashboard_asStudent_returnsOk() throws Exception {
        UserAccount user = new UserAccount(
            "student@test.com",
            "pass",
            Role.STUDENT
        );
        when(
            managementRepository.findUserByUsername("student@test.com")
        ).thenReturn(Optional.of(user));

        mockMvc
            .perform(get("/dashboard"))
            .andExpect(status().isOk())
            .andExpect(view().name("dashboard"))
            .andExpect(model().attribute("role", "STUDENT"));
    }

    @Test
    @WithMockUser(username = "teacher@test.com", roles = "TEACHER")
    void dashboard_asTeacher_includesCounts() throws Exception {
        UserAccount user = new UserAccount(
            "teacher@test.com",
            "pass",
            Role.TEACHER
        );
        when(
            managementRepository.findUserByUsername("teacher@test.com")
        ).thenReturn(Optional.of(user));
        when(managementService.getStudentCount()).thenReturn(5L);
        when(managementService.getTeacherCount()).thenReturn(2L);
        when(managementService.getCourseCount()).thenReturn(8L);

        mockMvc
            .perform(get("/dashboard"))
            .andExpect(status().isOk())
            .andExpect(model().attribute("studentCount", 5L))
            .andExpect(model().attribute("teacherCount", 2L))
            .andExpect(model().attribute("courseCount", 8L));
    }

    @Test
    @WithMockUser(username = "student@test.com", roles = "STUDENT")
    void viewProfile_asStudentWithLinkedEntity_returnsProfileView()
        throws Exception {
        UserAccount user = new UserAccount(
            "student@test.com",
            "pass",
            Role.STUDENT
        );
        user.setStudentId(1L);
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setEmail("student@test.com");

        when(
            managementRepository.findUserByUsername("student@test.com")
        ).thenReturn(Optional.of(user));
        when(managementService.getStudentById(1L)).thenReturn(student);

        mockMvc
            .perform(get("/profile"))
            .andExpect(status().isOk())
            .andExpect(view().name("profile/view"))
            .andExpect(model().attributeExists("student"))
            .andExpect(model().attribute("isStudent", true));
    }

    @Test
    @WithMockUser(username = "teacher@test.com", roles = "TEACHER")
    void viewProfile_asTeacherWithLinkedEntity_returnsProfileView()
        throws Exception {
        UserAccount user = new UserAccount(
            "teacher@test.com",
            "pass",
            Role.TEACHER
        );
        user.setTeacherId(1L);
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("Jane");

        when(
            managementRepository.findUserByUsername("teacher@test.com")
        ).thenReturn(Optional.of(user));
        when(managementService.getTeacherById(1L)).thenReturn(teacher);

        mockMvc
            .perform(get("/profile"))
            .andExpect(status().isOk())
            .andExpect(view().name("profile/view"))
            .andExpect(model().attribute("isTeacher", true));
    }

    @Test
    void viewProfile_unauthenticated_redirectsToLogin() throws Exception {
        mockMvc.perform(get("/profile")).andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    void accessDeniedPage_returnsView() throws Exception {
        mockMvc
            .perform(get("/access-denied"))
            .andExpect(status().isOk())
            .andExpect(view().name("access-denied"));
    }
}

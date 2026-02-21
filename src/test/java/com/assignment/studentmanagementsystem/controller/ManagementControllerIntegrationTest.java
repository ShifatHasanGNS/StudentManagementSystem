package com.assignment.studentmanagementsystem.controller;

import com.assignment.studentmanagementsystem.model.*;
import com.assignment.studentmanagementsystem.service.ManagementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// File path: src/test/java/com/assignment/studentmanagementsystem/controller/ManagementControllerIntegrationTest.java

@SpringBootTest
@AutoConfigureMockMvc
class ManagementControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ManagementService managementService;

    // --- Student endpoints ---

    @Test
    @WithMockUser(roles = "TEACHER")
    void listStudents_asTeacher_returnsOk() throws Exception {
        when(managementService.getAllStudents()).thenReturn(List.of());
        mockMvc.perform(get("/students"))
            .andExpect(status().isOk())
            .andExpect(view().name("entities/list"));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void listStudents_asStudent_returnsOk() throws Exception {
        when(managementService.getAllStudents()).thenReturn(List.of());
        mockMvc.perform(get("/students"))
            .andExpect(status().isOk());
    }

    @Test
    void listStudents_unauthenticated_redirectsToLogin() throws Exception {
        mockMvc.perform(get("/students"))
            .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void createStudentForm_asTeacher_returnsOk() throws Exception {
        when(managementService.getAllDepartments()).thenReturn(List.of());
        mockMvc.perform(get("/students/create"))
            .andExpect(status().isOk())
            .andExpect(view().name("entities/entity"));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void createStudentForm_asStudent_isForbidden() throws Exception {
        mockMvc.perform(get("/students/create"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void saveStudent_asTeacher_redirectsToList() throws Exception {
        Department dept = new Department("CS", "desc");
        dept.setId(1L);
        Student student = new Student();
        student.setId(1L);
        when(managementService.saveStudent(any())).thenReturn(student);

        mockMvc.perform(post("/students/save")
                .param("firstName", "John")
                .param("lastName", "Doe")
                .param("email", "john@test.com")
                .param("studentId", "S999")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/students"));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void deleteStudent_asTeacher_redirectsToList() throws Exception {
        doNothing().when(managementService).deleteStudent(1L);
        mockMvc.perform(get("/students/delete/1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/students"));
    }

    // --- Teacher endpoints ---

    @Test
    @WithMockUser(roles = "TEACHER")
    void listTeachers_asTeacher_returnsOk() throws Exception {
        when(managementService.getAllTeachers()).thenReturn(List.of());
        mockMvc.perform(get("/teachers"))
            .andExpect(status().isOk())
            .andExpect(view().name("entities/list"));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void listTeachers_asStudent_isForbidden() throws Exception {
        mockMvc.perform(get("/teachers"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void deleteTeacher_asTeacher_redirectsToList() throws Exception {
        doNothing().when(managementService).deleteTeacher(1L);
        mockMvc.perform(get("/teachers/delete/1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/teachers"));
    }

    // --- Course endpoints ---

    @Test
    @WithMockUser(roles = "TEACHER")
    void listCourses_asTeacher_returnsOk() throws Exception {
        when(managementService.getAllCourses()).thenReturn(List.of());
        mockMvc.perform(get("/courses"))
            .andExpect(status().isOk())
            .andExpect(view().name("entities/list"));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void listCourses_asStudent_isForbidden() throws Exception {
        mockMvc.perform(get("/courses"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void deleteCourse_asTeacher_redirectsToList() throws Exception {
        doNothing().when(managementService).deleteCourse(1L);
        mockMvc.perform(get("/courses/delete/1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/courses"));
    }

    // --- Department endpoints ---

    @Test
    @WithMockUser(roles = "TEACHER")
    void listDepartments_asTeacher_returnsOk() throws Exception {
        when(managementService.getAllDepartments()).thenReturn(List.of());
        mockMvc.perform(get("/departments"))
            .andExpect(status().isOk())
            .andExpect(view().name("entities/list"));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void listDepartments_asStudent_isForbidden() throws Exception {
        mockMvc.perform(get("/departments"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void deleteDepartment_asTeacher_redirectsToList() throws Exception {
        doNothing().when(managementService).deleteDepartment(1L);
        mockMvc.perform(get("/departments/delete/1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/departments"));
    }
}

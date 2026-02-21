package com.assignment.studentmanagementsystem;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.assignment.studentmanagementsystem.model.Student;
import com.assignment.studentmanagementsystem.model.Teacher;
import com.assignment.studentmanagementsystem.repository.ManagementRepository;
import com.assignment.studentmanagementsystem.service.ManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;

@ActiveProfiles("dev")
@SpringBootTest
public class ProfileEditWebIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();
    }

    @Autowired
    private ManagementRepository managementRepository;

    @Autowired
    private ManagementService managementService;

    @Test
    void studentProfileEditEndToEnd() throws Exception {
        // login
        MvcResult login = mockMvc.perform(formLogin().user("student@test.com").password("student123"))
            .andExpect(status().is3xxRedirection())
            .andExpect(authenticated().withUsername("student@test.com"))
            .andReturn();

        MockHttpSession session = (MockHttpSession) login.getRequest().getSession(false);

        // get edit form
        mockMvc.perform(get("/profile/edit").session(session)).andExpect(status().isOk());

        // submit save
        Long deptId = managementService.getAllDepartments().get(0).getId();
        mockMvc.perform(post("/profile/save/student").session(session)
                .with(csrf())
                .param("firstName", "E2EFirst")
                .param("lastName", "E2ELast")
                .param("email", "e2e@student.test")
                .param("studentId", "E2E-001")
                .param("department.id", String.valueOf(deptId))
            )
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/profile"));

        // view profile and assert model
        mockMvc.perform(get("/profile").session(session))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("student"))
            .andExpect(model().attribute("student", hasProperty("firstName", is("E2EFirst"))));

        // cleanup - remove created student and clear user link
        var user = managementRepository.findUserByUsername("student@test.com").get();
        Student s = managementService.getStudentById(user.getStudentId());
        managementService.deleteStudent(s.getId());
        user.setStudentId(null);
        managementRepository.saveUser(user);
    }

    @Test
    void teacherProfileEditEndToEnd() throws Exception {
        // login
        MvcResult login = mockMvc.perform(formLogin().user("teacher@test.com").password("teacher123"))
            .andExpect(status().is3xxRedirection())
            .andExpect(authenticated().withUsername("teacher@test.com"))
            .andReturn();

        MockHttpSession session = (MockHttpSession) login.getRequest().getSession(false);

        // get edit form
        mockMvc.perform(get("/profile/edit").session(session)).andExpect(status().isOk());

        // submit save
        Long deptId = managementService.getAllDepartments().get(0).getId();
        mockMvc.perform(post("/profile/save/teacher").session(session)
                .with(csrf())
                .param("firstName", "E2ET")
                .param("lastName", "Teacher")
                .param("email", "e2e@teacher.test")
                .param("employeeId", "EMP-E2E")
                .param("department.id", String.valueOf(deptId))
            )
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/profile"));

        // view profile and assert model
        mockMvc.perform(get("/profile").session(session))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("teacher"))
            .andExpect(model().attribute("teacher", hasProperty("firstName", is("E2ET"))));

        // cleanup - remove created teacher and clear user link
        var user = managementRepository.findUserByUsername("teacher@test.com").get();
        Teacher t = managementService.getTeacherById(user.getTeacherId());
        managementService.deleteTeacher(t.getId());
        user.setTeacherId(null);
        managementRepository.saveUser(user);
    }
}

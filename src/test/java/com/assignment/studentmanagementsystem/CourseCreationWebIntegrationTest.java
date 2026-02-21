package com.assignment.studentmanagementsystem;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.everyItem;

import com.assignment.studentmanagementsystem.model.Course;
import com.assignment.studentmanagementsystem.model.Teacher;
import com.assignment.studentmanagementsystem.model.Department;
import com.assignment.studentmanagementsystem.repository.ManagementRepository;
import com.assignment.studentmanagementsystem.service.ManagementService;
import org.junit.jupiter.api.AfterEach;
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
public class CourseCreationWebIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ManagementService managementService;

    @Autowired
    private ManagementRepository managementRepository;

    private Teacher createdTeacher;
    private Course createdCourse;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();

        // ensure there's at least one teacher entity to select in the course form
        Department dept = managementService.getAllDepartments().get(0);
        Teacher t = new Teacher();
        t.setFirstName("CourseTest");
        t.setLastName("Teacher");
        t.setEmail("coursetest@teacher.test");
        t.setEmployeeId("EMP-CT-001");
        t.setDepartment(dept);
        createdTeacher = managementService.saveTeacher(t);
    }

    @AfterEach
    void cleanup() {
        if (createdCourse != null && createdCourse.getId() != null) {
            try {
                managementService.deleteCourse(createdCourse.getId());
            } catch (RuntimeException ignored) {
                // already deleted by test flow
            }
            createdCourse = null;
        }
        if (createdTeacher != null && createdTeacher.getId() != null) {
            try {
                managementService.deleteTeacher(createdTeacher.getId());
            } catch (RuntimeException ignored) {
                // already deleted
            }
            createdTeacher = null;
        }
    }

    @Test
    void courseCreateEndToEnd() throws Exception {
        // login as teacher
        MvcResult login = mockMvc.perform(formLogin().user("teacher@test.com").password("teacher123"))
            .andExpect(status().is3xxRedirection())
            .andReturn();

        MockHttpSession session = (MockHttpSession) login.getRequest().getSession(false);

        // get create form
        mockMvc.perform(get("/courses/create").session(session)).andExpect(status().isOk());

        // submit create
        Long deptId = managementService.getAllDepartments().get(0).getId();
        mockMvc.perform(post("/courses/save").session(session)
                .with(csrf())
                .param("code", "CT-101")
                .param("name", "E2E Course")
                .param("description", "End-to-end course created by test")
                .param("department.id", String.valueOf(deptId))
                .param("teacher.id", String.valueOf(createdTeacher.getId()))
            )
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/courses"));

        // verify exists via repository
        var courses = managementService.getAllCourses();
        var found = courses.stream().filter(c -> "CT-101".equals(c.getCode())).findFirst();
        if (found.isPresent()) createdCourse = found.get();

        assert createdCourse != null;
        assert "E2E Course".equals(createdCourse.getName());

        // ensure list page contains action links
        mockMvc.perform(get("/courses").session(session))
            .andExpect(status().isOk())
            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content()
                .string(org.hamcrest.Matchers.containsString("/courses/view/" + createdCourse.getId())))
            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content()
                .string(org.hamcrest.Matchers.containsString("/courses/edit/" + createdCourse.getId())))
            .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content()
                .string(org.hamcrest.Matchers.containsString("/courses/delete/" + createdCourse.getId())));

        // view and edit pages should render
        mockMvc.perform(get("/courses/view/{id}", createdCourse.getId()).session(session)).andExpect(status().isOk());
        mockMvc.perform(get("/courses/edit/{id}", createdCourse.getId()).session(session)).andExpect(status().isOk());

        // delete should redirect and remove course
        mockMvc.perform(get("/courses/delete/{id}", createdCourse.getId()).session(session)).andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/courses"));

        var remaining = managementService.getAllCourses();
        assert remaining.stream().noneMatch(c -> c.getId().equals(createdCourse.getId()));
    }
}

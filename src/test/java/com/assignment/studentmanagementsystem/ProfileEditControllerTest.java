package com.assignment.studentmanagementsystem;

import com.assignment.studentmanagementsystem.controller.HomeController;
import com.assignment.studentmanagementsystem.model.Student;
import com.assignment.studentmanagementsystem.model.Teacher;
import com.assignment.studentmanagementsystem.repository.ManagementRepository;
import com.assignment.studentmanagementsystem.service.ManagementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("dev")
@SpringBootTest
public class ProfileEditControllerTest {

    @Autowired
    private HomeController homeController;

    @Autowired
    private ManagementRepository managementRepository;

    @Autowired
    private ManagementService managementService;

    @Test
    void editProfileFormForStudentReturnsForm() {
        SecurityContext previous = SecurityContextHolder.getContext();
        try {
            Authentication auth = new UsernamePasswordAuthenticationToken("student@test.com", "N/A");
            SecurityContext ctx = SecurityContextHolder.createEmptyContext();
            ctx.setAuthentication(auth);
            SecurityContextHolder.setContext(ctx);

            Model model = new ConcurrentModel();
            String view = homeController.editProfile(model);

            assertThat(view).isEqualTo("entities/entity");
            assertThat(model.containsAttribute("student")).isTrue();
            assertThat(model.getAttribute("mode")).isEqualTo("form");
        } finally {
            SecurityContextHolder.setContext(previous);
        }
    }

    @Test
    void saveProfileStudentPersistsChanges() {
        SecurityContext previous = SecurityContextHolder.getContext();
        try {
            Authentication auth = new UsernamePasswordAuthenticationToken("student@test.com", "N/A");
            SecurityContext ctx = SecurityContextHolder.createEmptyContext();
            ctx.setAuthentication(auth);
            SecurityContextHolder.setContext(ctx);

            // create a new student profile via profile save
            Student student = new Student();
            student.setFirstName("CreatedFirst");
            student.setLastName("CreatedLast");
            student.setEmail("created@student.test");
            student.setStudentId("STU-001");
            student.setDepartment(managementService.getAllDepartments().get(0));

            String redirect = homeController.saveProfileStudent(student);
            assertThat(redirect).isEqualTo("redirect:/profile");

            var user = managementRepository.findUserByUsername("student@test.com").get();
            assertThat(user.getStudentId()).isNotNull();

            Student reloaded = managementService.getStudentById(user.getStudentId());
            assertThat(reloaded.getFirstName()).isEqualTo("CreatedFirst");

            // verify profile view shows student details
            Model viewModel = new ConcurrentModel();
            String view = homeController.viewProfile(viewModel);
            assertThat(view).isEqualTo("profile/view");
            assertThat(viewModel.containsAttribute("student")).isTrue();
            Student shown = (Student) viewModel.getAttribute("student");
            assertThat(shown.getFirstName()).isEqualTo("CreatedFirst");

            // cleanup
            managementService.deleteStudent(reloaded.getId());
            user.setStudentId(null);
            managementRepository.saveUser(user);
        } finally {
            SecurityContextHolder.setContext(previous);
        }
    }

    @Test
    void editProfileFormForTeacherReturnsForm() {
        SecurityContext previous = SecurityContextHolder.getContext();
        try {
            Authentication auth = new UsernamePasswordAuthenticationToken("teacher@test.com", "N/A");
            SecurityContext ctx = SecurityContextHolder.createEmptyContext();
            ctx.setAuthentication(auth);
            SecurityContextHolder.setContext(ctx);

            Model model = new ConcurrentModel();
            String view = homeController.editProfile(model);

            assertThat(view).isEqualTo("entities/entity");
            assertThat(model.containsAttribute("teacher")).isTrue();
            assertThat(model.getAttribute("mode")).isEqualTo("form");
        } finally {
            SecurityContextHolder.setContext(previous);
        }
    }

    @Test
    void saveProfileTeacherPersistsChanges() {
        SecurityContext previous = SecurityContextHolder.getContext();
        try {
            Authentication auth = new UsernamePasswordAuthenticationToken("teacher@test.com", "N/A");
            SecurityContext ctx = SecurityContextHolder.createEmptyContext();
            ctx.setAuthentication(auth);
            SecurityContextHolder.setContext(ctx);

            // create a new teacher profile via profile save
            Teacher teacher = new Teacher();
            teacher.setFirstName("CreatedT");
            teacher.setLastName("Teacher");
            teacher.setEmail("created@teacher.test");
            teacher.setEmployeeId("EMP-100");
            teacher.setDepartment(managementService.getAllDepartments().get(0));

            String redirect = homeController.saveProfileTeacher(teacher);
            assertThat(redirect).isEqualTo("redirect:/profile");

            var user = managementRepository.findUserByUsername("teacher@test.com").get();
            assertThat(user.getTeacherId()).isNotNull();

            Teacher reloaded = managementService.getTeacherById(user.getTeacherId());
            assertThat(reloaded.getFirstName()).isEqualTo("CreatedT");

            // verify profile view shows teacher details
            Model viewModel = new ConcurrentModel();
            String view = homeController.viewProfile(viewModel);
            assertThat(view).isEqualTo("profile/view");
            assertThat(viewModel.containsAttribute("teacher")).isTrue();
            Teacher shown = (Teacher) viewModel.getAttribute("teacher");
            assertThat(shown.getFirstName()).isEqualTo("CreatedT");

            // cleanup
            managementService.deleteTeacher(reloaded.getId());
            user.setTeacherId(null);
            managementRepository.saveUser(user);
        } finally {
            SecurityContextHolder.setContext(previous);
        }
    }
}

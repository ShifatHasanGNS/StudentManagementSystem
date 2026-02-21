package com.assignment.studentmanagementsystem.controller;

import com.assignment.studentmanagementsystem.security.UserAccount;
import com.assignment.studentmanagementsystem.security.UserAccount.Role;
import com.assignment.studentmanagementsystem.repository.ManagementRepository;
import com.assignment.studentmanagementsystem.service.ManagementService;
import com.assignment.studentmanagementsystem.model.Student;
import com.assignment.studentmanagementsystem.model.Teacher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    private final ManagementService managementService;
    private final ManagementRepository managementRepository;

    public HomeController(ManagementService managementService, ManagementRepository managementRepository) {
        this.managementService = managementService;
        this.managementRepository = managementRepository;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            logger.debug("Dashboard accessed by user: {}", username);

            UserAccount user = managementRepository
                .findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

            model.addAttribute("username", username);
            model.addAttribute("role", user.getRole().name());

            if (user.getRole() == Role.TEACHER) {
                model.addAttribute("studentCount", managementService.getStudentCount());
                model.addAttribute("teacherCount", managementService.getTeacherCount());
                model.addAttribute("courseCount", managementService.getCourseCount());
            }

            return "dashboard";
        } catch (Exception e) {
            logger.error("Error in dashboard: {}", e.getMessage(), e);
            return "redirect:/login?error";
        }
    }

    @GetMapping("/profile")
    public String viewProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        UserAccount user = managementRepository.findUserByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found: " + username));

        model.addAttribute("user", user);

        // Always indicate role, but only populate entity details if present
        if (user.getRole() == Role.STUDENT) {
            model.addAttribute("isStudent", true);
            if (user.getStudentId() != null) {
                Student student = managementService.getStudentById(user.getStudentId());
                model.addAttribute("student", student);
            }
        } else if (user.getRole() == Role.TEACHER) {
            model.addAttribute("isTeacher", true);
            if (user.getTeacherId() != null) {
                Teacher teacher = managementService.getTeacherById(user.getTeacherId());
                model.addAttribute("teacher", teacher);
            }
        }

        return "profile/view";
    }

    @GetMapping("/profile/edit")
    public String editProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        UserAccount user = managementRepository.findUserByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found: " + username));

        if (user.getRole() == Role.STUDENT) {
            Student student;
            if (user.getStudentId() != null) {
                student = managementService.getStudentById(user.getStudentId());
            } else {
                // allow creating a student profile for users without one
                student = new Student();
            }
            model.addAttribute("student", student);
            model.addAttribute("entity", student);
            model.addAttribute("departments", managementService.getAllDepartments());
            model.addAttribute("entityType", "student");
            model.addAttribute("saveUrl", "/profile/save/student");
            model.addAttribute("listUrl", "/profile");
            model.addAttribute("mode", "form");
            return "entities/entity";
        } else if (user.getRole() == Role.TEACHER) {
            Teacher teacher;
            if (user.getTeacherId() != null) {
                teacher = managementService.getTeacherById(user.getTeacherId());
            } else {
                teacher = new Teacher();
            }
            model.addAttribute("teacher", teacher);
            model.addAttribute("entity", teacher);
            model.addAttribute("departments", managementService.getAllDepartments());
            model.addAttribute("entityType", "teacher");
            model.addAttribute("saveUrl", "/profile/save/teacher");
            model.addAttribute("listUrl", "/profile");
            model.addAttribute("mode", "form");
            return "entities/entity";
        } else {
            return "redirect:/profile?error";
        }
    }

    @PostMapping("/profile/save/student")
    public String saveProfileStudent(@ModelAttribute Student student) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserAccount user = managementRepository.findUserByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found: " + username));

        if (user.getStudentId() == null) {
            // create a new student profile and attach to user
            Student saved = managementService.saveStudent(student);
            saved.setUser(user);
            managementService.saveStudent(saved);
            user.setStudentId(saved.getId());
            managementRepository.saveUser(user);
        } else {
            if (!user.getStudentId().equals(student.getId())) {
                throw new RuntimeException("Not authorized to edit this student");
            }
            managementService.saveStudent(student);
        }

        return "redirect:/profile";
    }

    @PostMapping("/profile/save/teacher")
    public String saveProfileTeacher(@ModelAttribute Teacher teacher) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserAccount user = managementRepository.findUserByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found: " + username));

        if (user.getTeacherId() == null) {
            Teacher saved = managementService.saveTeacher(teacher);
            saved.setUser(user);
            managementService.saveTeacher(saved);
            user.setTeacherId(saved.getId());
            managementRepository.saveUser(user);
        } else {
            if (!user.getTeacherId().equals(teacher.getId())) {
                throw new RuntimeException("Not authorized to edit this teacher");
            }
            managementService.saveTeacher(teacher);
        }

        return "redirect:/profile";
    }

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return (
            "✅ Application is running! Endpoints:\n" +
            "- /login - Login page\n" +
            "- /register - Registration\n" +
            "- /dashboard - Dashboard (after login)\n" +
            "- /students - Student list (teacher only)"
        );
    }

    @GetMapping("/health")
    @ResponseBody
    public String health() {
        return "OK";
    }
}

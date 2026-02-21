package com.assignment.studentmanagementsystem.controller;

import com.assignment.studentmanagementsystem.security.UserAccount;
import com.assignment.studentmanagementsystem.security.UserAccount.Role;
import com.assignment.studentmanagementsystem.repository.ManagementRepository;
import com.assignment.studentmanagementsystem.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;
    private final ManagementRepository managementRepository;

    public RegistrationController(UserService userService, ManagementRepository managementRepository) {
        this.userService = userService;
        this.managementRepository = managementRepository;
    }

    @GetMapping
    public String showRegistrationForm() {
        return "register/choose-role";
    }

    @GetMapping("/student")
    public String showStudentRegistrationForm(Model model) {
        model.addAttribute("user", new UserAccount());
        return "register/student";
    }

    @GetMapping("/teacher")
    public String showTeacherRegistrationForm(Model model) {
        model.addAttribute("user", new UserAccount());
        return "register/teacher";
    }

    @PostMapping("/student")
    public String registerStudent(
        @RequestParam String username,
        @RequestParam String password,
        Model model
    ) {
        try {
            userService.registerUser(username, password, Role.STUDENT);
            return "redirect:/login?registered";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register/student";
        }
    }

    @PostMapping("/teacher")
    public String registerTeacher(
        @RequestParam String username,
        @RequestParam String password,
        Model model
    ) {
        try {
            userService.registerUser(username, password, Role.TEACHER);
            return "redirect:/login?registered";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register/teacher";
        }
    }

    // Keep legacy /register/profile for backwards compatibility by redirecting to canonical /profile
    @GetMapping(path = "/profile", produces = "text/html")
    public String profileRedirect() {
        return "redirect:/profile";
    }
}

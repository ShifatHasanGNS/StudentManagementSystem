package com.assignment.studentmanagementsystem;

import com.assignment.studentmanagementsystem.controller.HomeController;
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
public class ProfileControllerTest {

    @Autowired
    private HomeController homeController;

    @Test
    void viewProfileReturnsViewAndModelForAuthenticatedUser() {
        SecurityContext previous = SecurityContextHolder.getContext();
        try {
            Authentication auth = new UsernamePasswordAuthenticationToken("student@test.com", "N/A");
            SecurityContext ctx = SecurityContextHolder.createEmptyContext();
            ctx.setAuthentication(auth);
            SecurityContextHolder.setContext(ctx);

            Model model = new ConcurrentModel();
            String view = homeController.viewProfile(model);

            assertThat(view).isEqualTo("profile/view");
            assertThat(model.containsAttribute("user")).isTrue();
        } finally {
            SecurityContextHolder.setContext(previous);
        }
    }
}

package com.assignment.studentmanagementsystem;

import com.assignment.studentmanagementsystem.model.*;
import com.assignment.studentmanagementsystem.security.UserAccount;
import com.assignment.studentmanagementsystem.model.Department;
import com.assignment.studentmanagementsystem.repository.ManagementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    private final ManagementRepository managementRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(ManagementRepository managementRepository, PasswordEncoder passwordEncoder) {
        this.managementRepository = managementRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Create default department
        if (managementRepository.countDepartments() == 0) {
            Department computerScience = new Department();
            computerScience.setName("Computer Science");
            computerScience.setDescription(
                "Department of Computer Science and Engineering"
            );
            managementRepository.saveDepartment(computerScience);

            Department mathematics = new Department();
            mathematics.setName("Mathematics");
            mathematics.setDescription("Department of Mathematics");
            managementRepository.saveDepartment(mathematics);

            logger.info("Created default departments");
        }

        // Create test student user
        if (!managementRepository.existsByUsername("student@test.com")) {
            UserAccount studentUser = new UserAccount();
            studentUser.setUsername("student@test.com");
            studentUser.setPassword(passwordEncoder.encode("student123"));
            studentUser.setRole(UserAccount.Role.STUDENT);
            managementRepository.saveUser(studentUser);
            logger.info("Created student user: student@test.com / student123");

            // Create a corresponding Student entity for the seeded user (if none exist)
            if (managementRepository.countStudents() == 0) {
                Student student = new Student();
                student.setFirstName("Default");
                student.setLastName("Student");
                student.setEmail("student@test.com");
                student.setDepartment(managementRepository.findAllDepartments().get(0));
                student.setStudentId("S1001");
                managementRepository.saveStudent(student);
                logger.info("Created default Student entity");
            }
        }

        // Create test teacher user
        if (!managementRepository.existsByUsername("teacher@test.com")) {
            UserAccount teacherUser = new UserAccount();
            teacherUser.setUsername("teacher@test.com");
            teacherUser.setPassword(passwordEncoder.encode("teacher123"));
            teacherUser.setRole(UserAccount.Role.TEACHER);
            managementRepository.saveUser(teacherUser);
            logger.info("Created teacher user: teacher@test.com / teacher123");

            // Create a corresponding Teacher entity for the seeded user (if none exist)
            if (managementRepository.countTeachers() == 0) {
                Teacher teacher = new Teacher();
                teacher.setFirstName("Default");
                teacher.setLastName("Teacher");
                teacher.setEmail("teacher@test.com");
                teacher.setDepartment(managementRepository.findAllDepartments().get(0));
                teacher.setEmployeeId("T1001");
                managementRepository.saveTeacher(teacher);
                logger.info("Created default Teacher entity");
            }
        }

        // Create admin user
        if (!managementRepository.existsByUsername("admin@test.com")) {
            UserAccount adminUser = new UserAccount();
            adminUser.setUsername("admin@test.com");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setRole(UserAccount.Role.TEACHER);
            managementRepository.saveUser(adminUser);
            logger.info("Created admin user: admin@test.com / admin123");
        }

        // Ensure default Teacher and Student entities exist and are linked to seeded users
        if (managementRepository.countTeachers() == 0) {
            Teacher teacher = new Teacher();
            teacher.setFirstName("Default");
            teacher.setLastName("Teacher");
            teacher.setEmail("teacher@test.com");
            teacher.setDepartment(managementRepository.findAllDepartments().get(0));
            teacher.setEmployeeId("T1001");
            managementRepository.saveTeacher(teacher);
            logger.info("Created default Teacher entity (post-check)");
        }

        if (managementRepository.countStudents() == 0) {
            Student student = new Student();
            student.setFirstName("Default");
            student.setLastName("Student");
            student.setEmail("student@test.com");
            student.setDepartment(managementRepository.findAllDepartments().get(0));
            student.setStudentId("S1001");
            managementRepository.saveStudent(student);
            logger.info("Created default Student entity (post-check)");
        }

        logger.info("Data loading completed!");
    }
}

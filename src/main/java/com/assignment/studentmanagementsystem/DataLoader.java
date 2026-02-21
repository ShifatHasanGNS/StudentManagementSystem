package com.assignment.studentmanagementsystem;

import com.assignment.studentmanagementsystem.model.*;
import com.assignment.studentmanagementsystem.repository.ManagementRepository;
import com.assignment.studentmanagementsystem.security.UserAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class DataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(
        DataLoader.class
    );

    private final ManagementRepository managementRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(
        ManagementRepository managementRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.managementRepository = managementRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
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

        Department defaultDept = managementRepository
            .findAllDepartments()
            .get(0);

        if (!managementRepository.existsByUsername("student@test.com")) {
            UserAccount studentUser = new UserAccount();
            studentUser.setUsername("student@test.com");
            studentUser.setPassword(passwordEncoder.encode("student123"));
            studentUser.setRole(UserAccount.Role.STUDENT);

            Student student = new Student();
            student.setFirstName("Default");
            student.setLastName("Student");
            student.setEmail("student@test.com");
            student.setDepartment(defaultDept);
            student.setStudentId("S1001");
            Student savedStudent = managementRepository.saveStudent(student);

            studentUser.setStudentId(savedStudent.getId());
            UserAccount savedStudentUser = managementRepository.saveUser(
                studentUser
            );
            savedStudent.setUser(savedStudentUser);
            managementRepository.saveStudent(savedStudent);

            logger.info("Created student user: student@test.com / student123");
        }

        if (!managementRepository.existsByUsername("teacher@test.com")) {
            UserAccount teacherUser = new UserAccount();
            teacherUser.setUsername("teacher@test.com");
            teacherUser.setPassword(passwordEncoder.encode("teacher123"));
            teacherUser.setRole(UserAccount.Role.TEACHER);

            Teacher teacher = new Teacher();
            teacher.setFirstName("Default");
            teacher.setLastName("Teacher");
            teacher.setEmail("teacher@test.com");
            teacher.setDepartment(defaultDept);
            teacher.setEmployeeId("T1001");
            Teacher savedTeacher = managementRepository.saveTeacher(teacher);

            teacherUser.setTeacherId(savedTeacher.getId());
            UserAccount savedTeacherUser = managementRepository.saveUser(
                teacherUser
            );
            savedTeacher.setUser(savedTeacherUser);
            managementRepository.saveTeacher(savedTeacher);

            logger.info("Created teacher user: teacher@test.com / teacher123");
        }

        if (!managementRepository.existsByUsername("admin@test.com")) {
            UserAccount adminUser = new UserAccount();
            adminUser.setUsername("admin@test.com");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setRole(UserAccount.Role.TEACHER);
            managementRepository.saveUser(adminUser);
            logger.info("Created admin user: admin@test.com / admin123");
        }

        logger.info("Data loading completed!");
    }
}

package com.assignment.studentmanagementsystem.repository;

import com.assignment.studentmanagementsystem.model.*;
import com.assignment.studentmanagementsystem.security.UserAccount;
import com.assignment.studentmanagementsystem.security.UserAccount.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

// File path: src/test/java/com/assignment/studentmanagementsystem/repository/ManagementRepositoryIntegrationTest.java

@DataJpaTest
@Import(ManagementRepository.class)
class ManagementRepositoryIntegrationTest {

    @Autowired
    private ManagementRepository managementRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Department department;

    @BeforeEach
    void setUp() {
        department = new Department("Computer Science", "CS Dept");
        entityManager.persistAndFlush(department);
    }

    // -------------------------------------------------------------------------
    // Department
    // -------------------------------------------------------------------------

    @Test
    void saveDepartment_persist_assignsId() {
        Department dept = new Department("Mathematics", "Math Dept");
        Department saved = managementRepository.saveDepartment(dept);
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void saveDepartment_merge_updatesExisting() {
        department.setDescription("Updated description");
        Department updated = managementRepository.saveDepartment(department);
        entityManager.flush();
        entityManager.clear();

        Department fetched = entityManager.find(Department.class, updated.getId());
        assertThat(fetched.getDescription()).isEqualTo("Updated description");
    }

    @Test
    void findAllDepartments_returnsAll() {
        entityManager.persistAndFlush(new Department("Physics", "Physics Dept"));
        List<Department> result = managementRepository.findAllDepartments();
        assertThat(result).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void findDepartmentById_found_returnsDepartment() {
        Optional<Department> result = managementRepository.findDepartmentById(department.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Computer Science");
    }

    @Test
    void findDepartmentById_notFound_returnsEmpty() {
        Optional<Department> result = managementRepository.findDepartmentById(999L);
        assertThat(result).isEmpty();
    }

    @Test
    void deleteDepartmentById_removesFromDatabase() {
        Department dept = new Department("Temp", "Temp dept");
        entityManager.persistAndFlush(dept);

        managementRepository.deleteDepartmentById(dept.getId());
        entityManager.flush();
        entityManager.clear();

        assertThat(entityManager.find(Department.class, dept.getId())).isNull();
    }

    @Test
    void countDepartments_returnsCorrectCount() {
        long before = managementRepository.countDepartments();
        entityManager.persistAndFlush(new Department("Biology", "Bio Dept"));
        assertThat(managementRepository.countDepartments()).isEqualTo(before + 1);
    }

    // -------------------------------------------------------------------------
    // Student
    // -------------------------------------------------------------------------

    @Test
    void saveStudent_persist_assignsId() {
        Student student = makeStudent("Alice", "Smith", "alice@test.com", "S001");
        Student saved = managementRepository.saveStudent(student);
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void saveStudent_merge_updatesExisting() {
        Student student = makeStudent("Bob", "Jones", "bob@test.com", "S002");
        entityManager.persistAndFlush(student);

        student.setFirstName("Robert");
        managementRepository.saveStudent(student);
        entityManager.flush();
        entityManager.clear();

        Student fetched = entityManager.find(Student.class, student.getId());
        assertThat(fetched.getFirstName()).isEqualTo("Robert");
    }

    @Test
    void findAllStudents_returnsAll() {
        entityManager.persistAndFlush(makeStudent("C", "D", "cd@test.com", "S003"));
        entityManager.persistAndFlush(makeStudent("E", "F", "ef@test.com", "S004"));
        assertThat(managementRepository.findAllStudents()).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void findStudentById_found_returnsStudent() {
        Student student = makeStudent("John", "Doe", "john@test.com", "S005");
        entityManager.persistAndFlush(student);

        Optional<Student> result = managementRepository.findStudentById(student.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("john@test.com");
    }

    @Test
    void findStudentById_notFound_returnsEmpty() {
        assertThat(managementRepository.findStudentById(999L)).isEmpty();
    }

    @Test
    void deleteStudentById_removesFromDatabase() {
        Student student = makeStudent("Del", "Me", "del@test.com", "S006");
        entityManager.persistAndFlush(student);

        managementRepository.deleteStudentById(student.getId());
        entityManager.flush();
        entityManager.clear();

        assertThat(entityManager.find(Student.class, student.getId())).isNull();
    }

    @Test
    void deleteStudentById_nonExistentId_doesNothing() {
        assertThatCode(() -> managementRepository.deleteStudentById(999L))
            .doesNotThrowAnyException();
    }

    @Test
    void countStudents_returnsCorrectCount() {
        long before = managementRepository.countStudents();
        entityManager.persistAndFlush(makeStudent("X", "Y", "xy@test.com", "S007"));
        assertThat(managementRepository.countStudents()).isEqualTo(before + 1);
    }

    // -------------------------------------------------------------------------
    // Teacher
    // -------------------------------------------------------------------------

    @Test
    void saveTeacher_persist_assignsId() {
        Teacher teacher = makeTeacher("Prof", "Smith", "prof@test.com", "T001");
        Teacher saved = managementRepository.saveTeacher(teacher);
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void saveTeacher_merge_updatesExisting() {
        Teacher teacher = makeTeacher("Old", "Name", "old@test.com", "T002");
        entityManager.persistAndFlush(teacher);

        teacher.setFirstName("New");
        managementRepository.saveTeacher(teacher);
        entityManager.flush();
        entityManager.clear();

        assertThat(entityManager.find(Teacher.class, teacher.getId()).getFirstName()).isEqualTo("New");
    }

    @Test
    void findAllTeachers_returnsAll() {
        entityManager.persistAndFlush(makeTeacher("A", "B", "ab@test.com", "T003"));
        assertThat(managementRepository.findAllTeachers()).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    void findTeacherById_found_returnsTeacher() {
        Teacher teacher = makeTeacher("Jane", "Doe", "jane@test.com", "T004");
        entityManager.persistAndFlush(teacher);

        Optional<Teacher> result = managementRepository.findTeacherById(teacher.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getEmployeeId()).isEqualTo("T004");
    }

    @Test
    void findTeacherById_notFound_returnsEmpty() {
        assertThat(managementRepository.findTeacherById(999L)).isEmpty();
    }

    @Test
    void deleteTeacherById_removesFromDatabase() {
        Teacher teacher = makeTeacher("Del", "Me", "delteacher@test.com", "T005");
        entityManager.persistAndFlush(teacher);

        managementRepository.deleteTeacherById(teacher.getId());
        entityManager.flush();
        entityManager.clear();

        assertThat(entityManager.find(Teacher.class, teacher.getId())).isNull();
    }

    @Test
    void countTeachers_returnsCorrectCount() {
        long before = managementRepository.countTeachers();
        entityManager.persistAndFlush(makeTeacher("X", "Y", "xy2@test.com", "T006"));
        assertThat(managementRepository.countTeachers()).isEqualTo(before + 1);
    }

    // -------------------------------------------------------------------------
    // Course
    // -------------------------------------------------------------------------

    @Test
    void saveCourse_persist_assignsId() {
        Teacher teacher = makeTeacher("Prof", "X", "profx@test.com", "T007");
        entityManager.persistAndFlush(teacher);

        Course course = makeCourse("CS101", "Intro to CS", teacher);
        Course saved = managementRepository.saveCourse(course);
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void findAllCourses_returnsAll() {
        Teacher teacher = makeTeacher("Prof", "Y", "profy@test.com", "T008");
        entityManager.persistAndFlush(teacher);
        entityManager.persistAndFlush(makeCourse("CS102", "Data Structures", teacher));

        assertThat(managementRepository.findAllCourses()).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    void findCourseById_found_returnsCourse() {
        Teacher teacher = makeTeacher("Prof", "Z", "profz@test.com", "T009");
        entityManager.persistAndFlush(teacher);
        Course course = makeCourse("CS103", "Algorithms", teacher);
        entityManager.persistAndFlush(course);

        Optional<Course> result = managementRepository.findCourseById(course.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getCode()).isEqualTo("CS103");
    }

    @Test
    void findCourseById_notFound_returnsEmpty() {
        assertThat(managementRepository.findCourseById(999L)).isEmpty();
    }

    @Test
    void deleteCourseById_removesFromDatabase() {
        Teacher teacher = makeTeacher("Prof", "W", "profw@test.com", "T010");
        entityManager.persistAndFlush(teacher);
        Course course = makeCourse("CS104", "OS", teacher);
        entityManager.persistAndFlush(course);

        managementRepository.deleteCourseById(course.getId());
        entityManager.flush();
        entityManager.clear();

        assertThat(entityManager.find(Course.class, course.getId())).isNull();
    }

    @Test
    void countCourses_returnsCorrectCount() {
        Teacher teacher = makeTeacher("Prof", "V", "profv@test.com", "T011");
        entityManager.persistAndFlush(teacher);
        long before = managementRepository.countCourses();
        entityManager.persistAndFlush(makeCourse("CS105", "Networks", teacher));
        assertThat(managementRepository.countCourses()).isEqualTo(before + 1);
    }

    // -------------------------------------------------------------------------
    // UserAccount
    // -------------------------------------------------------------------------

    @Test
    void saveUser_persist_assignsId() {
        UserAccount user = makeUser("newuser@test.com", Role.STUDENT);
        UserAccount saved = managementRepository.saveUser(user);
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void findUserByUsername_found_returnsUser() {
        entityManager.persistAndFlush(makeUser("findme@test.com", Role.STUDENT));

        Optional<UserAccount> result = managementRepository.findUserByUsername("findme@test.com");
        assertThat(result).isPresent();
        assertThat(result.get().getRole()).isEqualTo(Role.STUDENT);
    }

    @Test
    void findUserByUsername_notFound_returnsEmpty() {
        assertThat(managementRepository.findUserByUsername("ghost@test.com")).isEmpty();
    }

    @Test
    void existsByUsername_exists_returnsTrue() {
        entityManager.persistAndFlush(makeUser("exists@test.com", Role.TEACHER));
        assertThat(managementRepository.existsByUsername("exists@test.com")).isTrue();
    }

    @Test
    void existsByUsername_notExists_returnsFalse() {
        assertThat(managementRepository.existsByUsername("nobody@test.com")).isFalse();
    }

    @Test
    void saveUser_merge_updatesExisting() {
        UserAccount user = makeUser("update@test.com", Role.STUDENT);
        entityManager.persistAndFlush(user);

        user.setRole(Role.TEACHER);
        managementRepository.saveUser(user);
        entityManager.flush();
        entityManager.clear();

        UserAccount fetched = entityManager.find(UserAccount.class, user.getId());
        assertThat(fetched.getRole()).isEqualTo(Role.TEACHER);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private Student makeStudent(String first, String last, String email, String studentId) {
        Student s = new Student();
        s.setFirstName(first);
        s.setLastName(last);
        s.setEmail(email);
        s.setStudentId(studentId);
        s.setDepartment(department);
        return s;
    }

    private Teacher makeTeacher(String first, String last, String email, String employeeId) {
        Teacher t = new Teacher();
        t.setFirstName(first);
        t.setLastName(last);
        t.setEmail(email);
        t.setEmployeeId(employeeId);
        t.setDepartment(department);
        return t;
    }

    private Course makeCourse(String code, String name, Teacher teacher) {
        Course c = new Course();
        c.setCode(code);
        c.setName(name);
        c.setDepartment(department);
        c.setTeacher(teacher);
        return c;
    }

    private UserAccount makeUser(String username, Role role) {
        UserAccount u = new UserAccount();
        u.setUsername(username);
        u.setPassword("encoded_password");
        u.setRole(role);
        return u;
    }
}

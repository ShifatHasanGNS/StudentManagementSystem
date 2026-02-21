package com.assignment.studentmanagementsystem.repository;

import static org.assertj.core.api.Assertions.*;

import com.assignment.studentmanagementsystem.model.*;
import com.assignment.studentmanagementsystem.security.UserAccount;
import com.assignment.studentmanagementsystem.security.UserAccount.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(
    properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
    }
)
class ManagementRepositoryIntegrationTest {

    @Autowired
    private ManagementRepository managementRepository;

    @PersistenceContext
    private EntityManager em;

    private Department department;

    @BeforeEach
    void setUp() {
        department = new Department("Computer Science", "CS Dept");
        em.persist(department);
        em.flush();
    }

    @Test
    void saveDepartment_persist_assignsId() {
        Department dept = new Department("Mathematics", "Math Dept");
        Department saved = managementRepository.saveDepartment(dept);
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void saveDepartment_merge_updatesExisting() {
        department.setDescription("Updated description");
        managementRepository.saveDepartment(department);
        em.flush();
        em.clear();

        Department fetched = em.find(Department.class, department.getId());
        assertThat(fetched.getDescription()).isEqualTo("Updated description");
    }

    @Test
    void findAllDepartments_returnsAll() {
        em.persist(new Department("Physics", "Physics Dept"));
        em.flush();
        List<Department> result = managementRepository.findAllDepartments();
        assertThat(result).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void findDepartmentById_found_returnsDepartment() {
        Optional<Department> result = managementRepository.findDepartmentById(
            department.getId()
        );
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Computer Science");
    }

    @Test
    void findDepartmentById_notFound_returnsEmpty() {
        assertThat(managementRepository.findDepartmentById(999L)).isEmpty();
    }

    @Test
    void deleteDepartmentById_removesFromDatabase() {
        Department dept = new Department("Temp", "Temp dept");
        em.persist(dept);
        em.flush();

        managementRepository.deleteDepartmentById(dept.getId());
        em.flush();
        em.clear();

        assertThat(em.find(Department.class, dept.getId())).isNull();
    }

    @Test
    void countDepartments_returnsCorrectCount() {
        long before = managementRepository.countDepartments();
        em.persist(new Department("Biology", "Bio Dept"));
        em.flush();
        assertThat(managementRepository.countDepartments()).isEqualTo(
            before + 1
        );
    }

    @Test
    void saveStudent_persist_assignsId() {
        Student saved = managementRepository.saveStudent(
            makeStudent("Alice", "Smith", "alice@test.com", "S001")
        );
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void saveStudent_merge_updatesExisting() {
        Student student = makeStudent("Bob", "Jones", "bob@test.com", "S002");
        em.persist(student);
        em.flush();

        student.setFirstName("Robert");
        managementRepository.saveStudent(student);
        em.flush();
        em.clear();

        assertThat(
            em.find(Student.class, student.getId()).getFirstName()
        ).isEqualTo("Robert");
    }

    @Test
    void findAllStudents_returnsAll() {
        em.persist(makeStudent("C", "D", "cd@test.com", "S003"));
        em.persist(makeStudent("E", "F", "ef@test.com", "S004"));
        em.flush();
        assertThat(
            managementRepository.findAllStudents()
        ).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void findStudentById_found_returnsStudent() {
        Student student = makeStudent("John", "Doe", "john@test.com", "S005");
        em.persist(student);
        em.flush();

        Optional<Student> result = managementRepository.findStudentById(
            student.getId()
        );
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
        em.persist(student);
        em.flush();

        managementRepository.deleteStudentById(student.getId());
        em.flush();
        em.clear();

        assertThat(em.find(Student.class, student.getId())).isNull();
    }

    @Test
    void deleteStudentById_nonExistentId_doesNothing() {
        assertThatCode(() ->
            managementRepository.deleteStudentById(999L)
        ).doesNotThrowAnyException();
    }

    @Test
    void countStudents_returnsCorrectCount() {
        long before = managementRepository.countStudents();
        em.persist(makeStudent("X", "Y", "xy@test.com", "S007"));
        em.flush();
        assertThat(managementRepository.countStudents()).isEqualTo(before + 1);
    }

    @Test
    void saveTeacher_persist_assignsId() {
        Teacher saved = managementRepository.saveTeacher(
            makeTeacher("Prof", "Smith", "prof@test.com", "T001")
        );
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void saveTeacher_merge_updatesExisting() {
        Teacher teacher = makeTeacher("Old", "Name", "old@test.com", "T002");
        em.persist(teacher);
        em.flush();

        teacher.setFirstName("New");
        managementRepository.saveTeacher(teacher);
        em.flush();
        em.clear();

        assertThat(
            em.find(Teacher.class, teacher.getId()).getFirstName()
        ).isEqualTo("New");
    }

    @Test
    void findTeacherById_found_returnsTeacher() {
        Teacher teacher = makeTeacher("Jane", "Doe", "jane@test.com", "T004");
        em.persist(teacher);
        em.flush();

        Optional<Teacher> result = managementRepository.findTeacherById(
            teacher.getId()
        );
        assertThat(result).isPresent();
        assertThat(result.get().getEmployeeId()).isEqualTo("T004");
    }

    @Test
    void findTeacherById_notFound_returnsEmpty() {
        assertThat(managementRepository.findTeacherById(999L)).isEmpty();
    }

    @Test
    void deleteTeacherById_removesFromDatabase() {
        Teacher teacher = makeTeacher(
            "Del",
            "Me",
            "delteacher@test.com",
            "T005"
        );
        em.persist(teacher);
        em.flush();

        managementRepository.deleteTeacherById(teacher.getId());
        em.flush();
        em.clear();

        assertThat(em.find(Teacher.class, teacher.getId())).isNull();
    }

    @Test
    void countTeachers_returnsCorrectCount() {
        long before = managementRepository.countTeachers();
        em.persist(makeTeacher("X", "Y", "xy2@test.com", "T006"));
        em.flush();
        assertThat(managementRepository.countTeachers()).isEqualTo(before + 1);
    }

    @Test
    void saveCourse_persist_assignsId() {
        Teacher teacher = makeTeacher("Prof", "X", "profx@test.com", "T007");
        em.persist(teacher);
        em.flush();

        Course saved = managementRepository.saveCourse(
            makeCourse("CS101", "Intro to CS", teacher)
        );
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void findCourseById_found_returnsCourse() {
        Teacher teacher = makeTeacher("Prof", "Z", "profz@test.com", "T009");
        em.persist(teacher);
        Course course = makeCourse("CS103", "Algorithms", teacher);
        em.persist(course);
        em.flush();

        Optional<Course> result = managementRepository.findCourseById(
            course.getId()
        );
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
        em.persist(teacher);
        Course course = makeCourse("CS104", "OS", teacher);
        em.persist(course);
        em.flush();

        managementRepository.deleteCourseById(course.getId());
        em.flush();
        em.clear();

        assertThat(em.find(Course.class, course.getId())).isNull();
    }

    @Test
    void countCourses_returnsCorrectCount() {
        Teacher teacher = makeTeacher("Prof", "V", "profv@test.com", "T011");
        em.persist(teacher);
        em.flush();
        long before = managementRepository.countCourses();
        em.persist(makeCourse("CS105", "Networks", teacher));
        em.flush();
        assertThat(managementRepository.countCourses()).isEqualTo(before + 1);
    }

    @Test
    void saveUser_persist_assignsId() {
        UserAccount saved = managementRepository.saveUser(
            makeUser("newuser@test.com", Role.STUDENT)
        );
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void findUserByUsername_found_returnsUser() {
        em.persist(makeUser("findme@test.com", Role.STUDENT));
        em.flush();

        Optional<UserAccount> result = managementRepository.findUserByUsername(
            "findme@test.com"
        );
        assertThat(result).isPresent();
        assertThat(result.get().getRole()).isEqualTo(Role.STUDENT);
    }

    @Test
    void findUserByUsername_notFound_returnsEmpty() {
        assertThat(
            managementRepository.findUserByUsername("ghost@test.com")
        ).isEmpty();
    }

    @Test
    void existsByUsername_exists_returnsTrue() {
        em.persist(makeUser("exists@test.com", Role.TEACHER));
        em.flush();
        assertThat(
            managementRepository.existsByUsername("exists@test.com")
        ).isTrue();
    }

    @Test
    void existsByUsername_notExists_returnsFalse() {
        assertThat(
            managementRepository.existsByUsername("nobody@test.com")
        ).isFalse();
    }

    @Test
    void saveUser_merge_updatesExisting() {
        UserAccount user = makeUser("update@test.com", Role.STUDENT);
        em.persist(user);
        em.flush();

        user.setRole(Role.TEACHER);
        managementRepository.saveUser(user);
        em.flush();
        em.clear();

        assertThat(
            em.find(UserAccount.class, user.getId()).getRole()
        ).isEqualTo(Role.TEACHER);
    }

    private Student makeStudent(
        String first,
        String last,
        String email,
        String studentId
    ) {
        Student s = new Student();
        s.setFirstName(first);
        s.setLastName(last);
        s.setEmail(email);
        s.setStudentId(studentId);
        s.setDepartment(department);
        return s;
    }

    private Teacher makeTeacher(
        String first,
        String last,
        String email,
        String employeeId
    ) {
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

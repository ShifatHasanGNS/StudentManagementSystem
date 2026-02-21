package com.assignment.studentmanagementsystem.service;

import com.assignment.studentmanagementsystem.model.*;
import com.assignment.studentmanagementsystem.repository.ManagementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

// File path: src/test/java/com/assignment/studentmanagementsystem/service/ManagementServiceTest.java

@ExtendWith(MockitoExtension.class)
class ManagementServiceTest {

    @Mock
    private ManagementRepository managementRepository;

    @InjectMocks
    private ManagementService managementService;

    private Student student;
    private Teacher teacher;
    private Course course;
    private Department department;

    @BeforeEach
    void setUp() {
        department = new Department("Computer Science", "CS Dept");
        department.setId(1L);

        student = new Student();
        student.setId(1L);
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setEmail("john@test.com");
        student.setStudentId("S001");
        student.setDepartment(department);

        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("Jane");
        teacher.setLastName("Smith");
        teacher.setEmail("jane@test.com");
        teacher.setEmployeeId("T001");
        teacher.setDepartment(department);

        course = new Course();
        course.setId(1L);
        course.setCode("CS101");
        course.setName("Intro to CS");
        course.setDepartment(department);
        course.setTeacher(teacher);
    }

    // --- Student Tests ---

    @Test
    void getAllStudents_returnsAll() {
        when(managementRepository.findAllStudents()).thenReturn(List.of(student));
        List<Student> result = managementService.getAllStudents();
        assertThat(result).hasSize(1).contains(student);
    }

    @Test
    void getStudentById_found_returnsStudent() {
        when(managementRepository.findStudentById(1L)).thenReturn(Optional.of(student));
        Student result = managementService.getStudentById(1L);
        assertThat(result.getEmail()).isEqualTo("john@test.com");
    }

    @Test
    void getStudentById_notFound_throwsException() {
        when(managementRepository.findStudentById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> managementService.getStudentById(99L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Student not found");
    }

    @Test
    void saveStudent_delegatesToRepository() {
        when(managementRepository.saveStudent(student)).thenReturn(student);
        Student result = managementService.saveStudent(student);
        assertThat(result).isEqualTo(student);
        verify(managementRepository).saveStudent(student);
    }

    @Test
    void deleteStudent_found_deletesSuccessfully() {
        when(managementRepository.findStudentById(1L)).thenReturn(Optional.of(student));
        managementService.deleteStudent(1L);
        verify(managementRepository).deleteStudentById(1L);
    }

    @Test
    void deleteStudent_notFound_throwsException() {
        when(managementRepository.findStudentById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> managementService.deleteStudent(99L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Student not found");
        verify(managementRepository, never()).deleteStudentById(any());
    }

    @Test
    void getStudentCount_returnsCount() {
        when(managementRepository.countStudents()).thenReturn(5L);
        assertThat(managementService.getStudentCount()).isEqualTo(5L);
    }

    // --- Teacher Tests ---

    @Test
    void getAllTeachers_returnsAll() {
        when(managementRepository.findAllTeachers()).thenReturn(List.of(teacher));
        assertThat(managementService.getAllTeachers()).hasSize(1).contains(teacher);
    }

    @Test
    void getTeacherById_found_returnsTeacher() {
        when(managementRepository.findTeacherById(1L)).thenReturn(Optional.of(teacher));
        assertThat(managementService.getTeacherById(1L).getEmail()).isEqualTo("jane@test.com");
    }

    @Test
    void getTeacherById_notFound_throwsException() {
        when(managementRepository.findTeacherById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> managementService.getTeacherById(99L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Teacher not found");
    }

    @Test
    void saveTeacher_delegatesToRepository() {
        when(managementRepository.saveTeacher(teacher)).thenReturn(teacher);
        assertThat(managementService.saveTeacher(teacher)).isEqualTo(teacher);
        verify(managementRepository).saveTeacher(teacher);
    }

    @Test
    void deleteTeacher_found_deletesSuccessfully() {
        when(managementRepository.findTeacherById(1L)).thenReturn(Optional.of(teacher));
        managementService.deleteTeacher(1L);
        verify(managementRepository).deleteTeacherById(1L);
    }

    @Test
    void deleteTeacher_notFound_throwsException() {
        when(managementRepository.findTeacherById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> managementService.deleteTeacher(99L))
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    void getTeacherCount_returnsCount() {
        when(managementRepository.countTeachers()).thenReturn(3L);
        assertThat(managementService.getTeacherCount()).isEqualTo(3L);
    }

    // --- Course Tests ---

    @Test
    void getAllCourses_returnsAll() {
        when(managementRepository.findAllCourses()).thenReturn(List.of(course));
        assertThat(managementService.getAllCourses()).hasSize(1).contains(course);
    }

    @Test
    void getCourseById_found_returnsCourse() {
        when(managementRepository.findCourseById(1L)).thenReturn(Optional.of(course));
        assertThat(managementService.getCourseById(1L).getCode()).isEqualTo("CS101");
    }

    @Test
    void getCourseById_notFound_throwsException() {
        when(managementRepository.findCourseById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> managementService.getCourseById(99L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Course not found");
    }

    @Test
    void saveCourse_delegatesToRepository() {
        when(managementRepository.saveCourse(course)).thenReturn(course);
        assertThat(managementService.saveCourse(course)).isEqualTo(course);
    }

    @Test
    void deleteCourse_found_deletesSuccessfully() {
        when(managementRepository.findCourseById(1L)).thenReturn(Optional.of(course));
        managementService.deleteCourse(1L);
        verify(managementRepository).deleteCourseById(1L);
    }

    @Test
    void getCourseCount_returnsCount() {
        when(managementRepository.countCourses()).thenReturn(10L);
        assertThat(managementService.getCourseCount()).isEqualTo(10L);
    }

    // --- Department Tests ---

    @Test
    void getAllDepartments_returnsAll() {
        when(managementRepository.findAllDepartments()).thenReturn(List.of(department));
        assertThat(managementService.getAllDepartments()).hasSize(1).contains(department);
    }

    @Test
    void getDepartmentById_found_returnsDepartment() {
        when(managementRepository.findDepartmentById(1L)).thenReturn(Optional.of(department));
        assertThat(managementService.getDepartmentById(1L).getName()).isEqualTo("Computer Science");
    }

    @Test
    void getDepartmentById_notFound_throwsException() {
        when(managementRepository.findDepartmentById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> managementService.getDepartmentById(99L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Department not found");
    }

    @Test
    void saveDepartment_delegatesToRepository() {
        when(managementRepository.saveDepartment(department)).thenReturn(department);
        assertThat(managementService.saveDepartment(department)).isEqualTo(department);
    }

    @Test
    void deleteDepartment_found_deletesSuccessfully() {
        when(managementRepository.findDepartmentById(1L)).thenReturn(Optional.of(department));
        managementService.deleteDepartment(1L);
        verify(managementRepository).deleteDepartmentById(1L);
    }

    @Test
    void getDepartmentCount_returnsCount() {
        when(managementRepository.countDepartments()).thenReturn(2L);
        assertThat(managementService.getDepartmentCount()).isEqualTo(2L);
    }
}

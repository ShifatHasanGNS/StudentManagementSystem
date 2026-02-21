package com.assignment.studentmanagementsystem.service;

import com.assignment.studentmanagementsystem.model.Course;
import com.assignment.studentmanagementsystem.model.Department;
import com.assignment.studentmanagementsystem.model.Student;
import com.assignment.studentmanagementsystem.model.Teacher;
import com.assignment.studentmanagementsystem.repository.ManagementRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ManagementService {

    private final ManagementRepository managementRepository;

    public ManagementService(ManagementRepository managementRepository) {
        this.managementRepository = managementRepository;
    }

    // Students
    public List<Student> getAllStudents() {
        return managementRepository.findAllStudents();
    }

    public Student getStudentById(Long id) {
        return managementRepository
            .findStudentById(id)
            .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
    }

    public Student saveStudent(Student student) {
        return managementRepository.saveStudent(student);
    }

    public void deleteStudent(Long id) {
        // if not found, the delete method will silently do nothing; keep old behavior
        Student s = managementRepository.findStudentById(id).orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        managementRepository.deleteStudentById(id);
    }

    public long getStudentCount() {
        return managementRepository.countStudents();
    }

    // Teachers
    public List<Teacher> getAllTeachers() {
        return managementRepository.findAllTeachers();
    }

    public Teacher getTeacherById(Long id) {
        return managementRepository
            .findTeacherById(id)
            .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));
    }

    public Teacher saveTeacher(Teacher teacher) {
        return managementRepository.saveTeacher(teacher);
    }

    public void deleteTeacher(Long id) {
        Teacher t = managementRepository.findTeacherById(id).orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));
        managementRepository.deleteTeacherById(id);
    }

    public long getTeacherCount() {
        return managementRepository.countTeachers();
    }

    // Courses
    public List<Course> getAllCourses() {
        return managementRepository.findAllCourses();
    }

    public Course getCourseById(Long id) {
        return managementRepository
            .findCourseById(id)
            .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
    }

    public Course saveCourse(Course course) {
        return managementRepository.saveCourse(course);
    }

    public void deleteCourse(Long id) {
        Course c = managementRepository.findCourseById(id).orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
        managementRepository.deleteCourseById(id);
    }

    public long getCourseCount() {
        return managementRepository.countCourses();
    }

    // Departments
    public List<Department> getAllDepartments() {
        return managementRepository.findAllDepartments();
    }

    public Department getDepartmentById(Long id) {
        return managementRepository
            .findDepartmentById(id)
            .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
    }

    public Department saveDepartment(Department department) {
        return managementRepository.saveDepartment(department);
    }

    public void deleteDepartment(Long id) {
        Department d = managementRepository.findDepartmentById(id).orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
        managementRepository.deleteDepartmentById(id);
    }

    public long getDepartmentCount() {
        return managementRepository.countDepartments();
    }
}
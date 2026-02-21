package com.assignment.studentmanagementsystem.controller;

import com.assignment.studentmanagementsystem.model.Course;
import com.assignment.studentmanagementsystem.model.Department;
import com.assignment.studentmanagementsystem.model.Student;
import com.assignment.studentmanagementsystem.model.Teacher;
import com.assignment.studentmanagementsystem.service.ManagementService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ManagementController {

    private static final Logger logger = LoggerFactory.getLogger(
        ManagementController.class
    );

    private final ManagementService managementService;

    public ManagementController(ManagementService managementService) {
        this.managementService = managementService;
    }

    @GetMapping("/students")
    @PreAuthorize("hasRole('TEACHER') or hasRole('STUDENT')")
    public String listStudents(Model model) {
        List<Student> students = managementService.getAllStudents();
        model.addAttribute("items", students);
        model.addAttribute("title", "Student List");
        model.addAttribute("createUrl", "/students/create");
        model.addAttribute("entityType", "student");
        return "entities/list";
    }

    @GetMapping("/students/create")
    @PreAuthorize("hasRole('TEACHER')")
    public String createStudentForm(Model model) {
        Student student = new Student();
        List<Department> departments = managementService.getAllDepartments();

        model.addAttribute("student", student);
        model.addAttribute("entity", student);
        model.addAttribute("departments", departments);
        model.addAttribute("entityType", "student");
        model.addAttribute("saveUrl", "/students/save");
        model.addAttribute("listUrl", "/students");
        model.addAttribute("mode", "form");
        return "entities/entity";
    }

    @PostMapping("/students/save")
    @PreAuthorize("hasRole('TEACHER')")
    public String saveStudent(@ModelAttribute Student student) {
        managementService.saveStudent(student);
        return "redirect:/students";
    }

    @GetMapping("/students/edit/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public String editStudentForm(@PathVariable Long id, Model model) {
        Student student = managementService.getStudentById(id);
        List<Department> departments = managementService.getAllDepartments();

        model.addAttribute("student", student);
        model.addAttribute("entity", student);
        model.addAttribute("departments", departments);
        model.addAttribute("entityType", "student");
        model.addAttribute("saveUrl", "/students/save");
        model.addAttribute("listUrl", "/students");
        model.addAttribute("mode", "form");
        return "entities/entity";
    }

    @GetMapping("/students/delete/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public String deleteStudent(@PathVariable Long id) {
        managementService.deleteStudent(id);
        return "redirect:/students";
    }

    @GetMapping("/students/view/{id}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('STUDENT')")
    public String viewStudent(@PathVariable Long id, Model model) {
        Student student = managementService.getStudentById(id);
        model.addAttribute("student", student);
        model.addAttribute("entity", student);
        model.addAttribute("entityType", "student");
        model.addAttribute("listUrl", "/students");
        model.addAttribute("editUrl", "/students/edit/" + id);
        model.addAttribute("mode", "view");
        return "entities/entity";
    }

    @GetMapping("/teachers")
    @PreAuthorize("hasRole('TEACHER')")
    public String listTeachers(Model model) {
        List<Teacher> teachers = managementService.getAllTeachers();
        model.addAttribute("items", teachers);
        model.addAttribute("title", "Teacher List");
        model.addAttribute("createUrl", "/teachers/create");
        model.addAttribute("entityType", "teacher");
        return "entities/list";
    }

    @GetMapping("/teachers/create")
    @PreAuthorize("hasRole('TEACHER')")
    public String createTeacherForm(Model model) {
        Teacher teacher = new Teacher();
        List<Department> departments = managementService.getAllDepartments();

        model.addAttribute("teacher", teacher);
        model.addAttribute("entity", teacher);
        model.addAttribute("departments", departments);
        model.addAttribute("entityType", "teacher");
        model.addAttribute("saveUrl", "/teachers/save");
        model.addAttribute("listUrl", "/teachers");
        model.addAttribute("mode", "form");
        return "entities/entity";
    }

    @PostMapping("/teachers/save")
    @PreAuthorize("hasRole('TEACHER')")
    public String saveTeacher(@ModelAttribute Teacher teacher) {
        managementService.saveTeacher(teacher);
        return "redirect:/teachers";
    }

    @GetMapping("/teachers/edit/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public String editTeacherForm(@PathVariable Long id, Model model) {
        Teacher teacher = managementService.getTeacherById(id);
        List<Department> departments = managementService.getAllDepartments();

        model.addAttribute("teacher", teacher);
        model.addAttribute("entity", teacher);
        model.addAttribute("departments", departments);
        model.addAttribute("entityType", "teacher");
        model.addAttribute("saveUrl", "/teachers/save");
        model.addAttribute("listUrl", "/teachers");
        model.addAttribute("mode", "form");
        return "entities/entity";
    }

    @GetMapping("/teachers/delete/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public String deleteTeacher(@PathVariable Long id) {
        managementService.deleteTeacher(id);
        return "redirect:/teachers";
    }

    @GetMapping("/teachers/view/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public String viewTeacher(@PathVariable Long id, Model model) {
        Teacher teacher = managementService.getTeacherById(id);
        model.addAttribute("teacher", teacher);
        model.addAttribute("entity", teacher);
        model.addAttribute("entityType", "teacher");
        model.addAttribute("listUrl", "/teachers");
        model.addAttribute("editUrl", "/teachers/edit/" + id);
        model.addAttribute("mode", "view");
        return "entities/entity";
    }

    @GetMapping("/courses")
    @PreAuthorize("hasRole('TEACHER')")
    public String listCourses(Model model) {
        List<Course> courses = managementService.getAllCourses();
        model.addAttribute("items", courses);
        model.addAttribute("title", "Courses");
        model.addAttribute("createUrl", "/courses/create");
        model.addAttribute("entityType", "course");
        return "entities/list";
    }

    @GetMapping("/courses/create")
    @PreAuthorize("hasRole('TEACHER')")
    public String createCourseForm(Model model) {
        Course course = new Course();
        List<Department> departments = managementService.getAllDepartments();
        List<Teacher> teachers = managementService.getAllTeachers();

        model.addAttribute("course", course);
        model.addAttribute("entity", course);
        model.addAttribute("departments", departments);
        model.addAttribute("teachers", teachers);
        model.addAttribute("entityType", "course");
        model.addAttribute("saveUrl", "/courses/save");
        model.addAttribute("listUrl", "/courses");
        model.addAttribute("mode", "form");
        return "entities/entity";
    }

    @PostMapping("/courses/save")
    @PreAuthorize("hasRole('TEACHER')")
    public String saveCourse(@ModelAttribute Course course) {
        managementService.saveCourse(course);
        return "redirect:/courses";
    }

    @GetMapping("/courses/edit/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public String editCourseForm(@PathVariable Long id, Model model) {
        Course course = managementService.getCourseById(id);
        List<Department> departments = managementService.getAllDepartments();
        List<Teacher> teachers = managementService.getAllTeachers();

        model.addAttribute("course", course);
        model.addAttribute("entity", course);
        model.addAttribute("departments", departments);
        model.addAttribute("teachers", teachers);
        model.addAttribute("entityType", "course");
        model.addAttribute("saveUrl", "/courses/save");
        model.addAttribute("listUrl", "/courses");
        model.addAttribute("mode", "form");
        return "entities/entity";
    }

    @GetMapping("/courses/delete/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public String deleteCourse(@PathVariable Long id) {
        managementService.deleteCourse(id);
        return "redirect:/courses";
    }

    @GetMapping("/courses/view/{id}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('STUDENT')")
    public String viewCourse(@PathVariable Long id, Model model) {
        Course course = managementService.getCourseById(id);
        model.addAttribute("course", course);
        model.addAttribute("entity", course);
        model.addAttribute("entityType", "course");
        model.addAttribute("listUrl", "/courses");
        model.addAttribute("editUrl", "/courses/edit/" + id);
        model.addAttribute("mode", "view");
        return "entities/entity";
    }

    @GetMapping("/departments")
    @PreAuthorize("hasRole('TEACHER')")
    public String listDepartments(Model model) {
        List<Department> departments = managementService.getAllDepartments();
        model.addAttribute("items", departments);
        model.addAttribute("title", "Department List");
        model.addAttribute("createUrl", "/departments/create");
        model.addAttribute("entityType", "department");
        return "entities/list";
    }

    @GetMapping("/departments/create")
    @PreAuthorize("hasRole('TEACHER')")
    public String createDepartmentForm(Model model) {
        Department dept = new Department();
        model.addAttribute("department", dept);
        model.addAttribute("entity", dept);
        model.addAttribute("entityType", "department");
        model.addAttribute("saveUrl", "/departments/save");
        model.addAttribute("listUrl", "/departments");
        model.addAttribute("mode", "form");
        return "entities/entity";
    }

    @PostMapping("/departments/save")
    @PreAuthorize("hasRole('TEACHER')")
    public String saveDepartment(@ModelAttribute Department department) {
        managementService.saveDepartment(department);
        return "redirect:/departments";
    }

    @GetMapping("/departments/edit/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public String editDepartmentForm(@PathVariable Long id, Model model) {
        Department department = managementService.getDepartmentById(id);
        model.addAttribute("department", department);
        model.addAttribute("entity", department);
        model.addAttribute("entityType", "department");
        model.addAttribute("saveUrl", "/departments/save");
        model.addAttribute("listUrl", "/departments");
        model.addAttribute("mode", "form");
        return "entities/entity";
    }

    @GetMapping("/departments/delete/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public String deleteDepartment(@PathVariable Long id) {
        managementService.deleteDepartment(id);
        return "redirect:/departments";
    }

    @GetMapping("/departments/view/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public String viewDepartment(@PathVariable Long id, Model model) {
        Department department = managementService.getDepartmentById(id);
        model.addAttribute("department", department);
        model.addAttribute("entity", department);
        model.addAttribute("entityType", "department");
        model.addAttribute("listUrl", "/departments");
        model.addAttribute("editUrl", "/departments/edit/" + id);
        model.addAttribute("mode", "view");
        return "entities/entity";
    }
}

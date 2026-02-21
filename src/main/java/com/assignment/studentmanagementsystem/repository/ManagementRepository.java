package com.assignment.studentmanagementsystem.repository;

import com.assignment.studentmanagementsystem.model.Course;
import com.assignment.studentmanagementsystem.model.Department;
import com.assignment.studentmanagementsystem.model.Student;
import com.assignment.studentmanagementsystem.model.Teacher;
import com.assignment.studentmanagementsystem.security.UserAccount;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class ManagementRepository {

    @PersistenceContext
    private EntityManager em;

    public List<Student> findAllStudents() {
        return em
            .createQuery("select s from Student s", Student.class)
            .getResultList();
    }

    public Optional<Student> findStudentById(Long id) {
        return Optional.ofNullable(em.find(Student.class, id));
    }

    public Student saveStudent(Student student) {
        if (student.getId() == null) {
            em.persist(student);
            return student;
        }
        return em.merge(student);
    }

    public void deleteStudentById(Long id) {
        Student s = em.find(Student.class, id);
        if (s != null) em.remove(s);
    }

    public long countStudents() {
        return em
            .createQuery("select count(s) from Student s", Long.class)
            .getSingleResult();
    }

    public List<Teacher> findAllTeachers() {
        return em
            .createQuery("select t from Teacher t", Teacher.class)
            .getResultList();
    }

    public Optional<Teacher> findTeacherById(Long id) {
        return Optional.ofNullable(em.find(Teacher.class, id));
    }

    public Teacher saveTeacher(Teacher teacher) {
        if (teacher.getId() == null) {
            em.persist(teacher);
            return teacher;
        }
        return em.merge(teacher);
    }

    public void deleteTeacherById(Long id) {
        Teacher t = em.find(Teacher.class, id);
        if (t != null) em.remove(t);
    }

    public long countTeachers() {
        return em
            .createQuery("select count(t) from Teacher t", Long.class)
            .getSingleResult();
    }

    public List<Course> findAllCourses() {
        return em
            .createQuery("select c from Course c", Course.class)
            .getResultList();
    }

    public Optional<Course> findCourseById(Long id) {
        return Optional.ofNullable(em.find(Course.class, id));
    }

    public Course saveCourse(Course course) {
        if (course.getId() == null) {
            em.persist(course);
            return course;
        }
        return em.merge(course);
    }

    public void deleteCourseById(Long id) {
        Course c = em.find(Course.class, id);
        if (c != null) em.remove(c);
    }

    public long countCourses() {
        return em
            .createQuery("select count(c) from Course c", Long.class)
            .getSingleResult();
    }

    public List<Department> findAllDepartments() {
        return em
            .createQuery("select d from Department d", Department.class)
            .getResultList();
    }

    public Optional<Department> findDepartmentById(Long id) {
        return Optional.ofNullable(em.find(Department.class, id));
    }

    public Department saveDepartment(Department department) {
        if (department.getId() == null) {
            em.persist(department);
            return department;
        }
        return em.merge(department);
    }

    public void deleteDepartmentById(Long id) {
        Department d = em.find(Department.class, id);
        if (d != null) em.remove(d);
    }

    public long countDepartments() {
        return em
            .createQuery("select count(d) from Department d", Long.class)
            .getSingleResult();
    }

    public Optional<UserAccount> findUserByUsername(String username) {
        List<UserAccount> list = em
            .createQuery(
                "select u from UserAccount u where u.username = :u",
                UserAccount.class
            )
            .setParameter("u", username)
            .setMaxResults(1)
            .getResultList();
        return list.stream().findFirst();
    }

    public boolean existsByUsername(String username) {
        Long count = em
            .createQuery(
                "select count(u) from UserAccount u where u.username = :u",
                Long.class
            )
            .setParameter("u", username)
            .getSingleResult();
        return count != null && count > 0;
    }

    public UserAccount saveUser(UserAccount user) {
        if (user.getId() == null) {
            em.persist(user);
            return user;
        }
        return em.merge(user);
    }
}

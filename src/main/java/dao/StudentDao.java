package dao;

import model.Course;
import model.Instructor;
import model.Student;

import java.util.List;

public interface StudentDao {
    void createStudent(Student student);
    Student getStudentByAccountId(int accountId);
    Student getStudentById(int studentId);
    List<Student> getAllStudents();
    List<Student> findStudentsByCourseAndInstructor(Course course, Instructor instructor);

    List<Student> getStudentsByMajor(String major);
    void updateStudent(Student student);
    void deleteStudent(int studentId);
}

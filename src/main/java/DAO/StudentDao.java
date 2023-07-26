package DAO;

import Model.Student;

import java.util.List;

public interface StudentDao {
    void createStudent(Student student);
    Student getStudentById(int studentId);
    List<Student> getAllStudents();
    List<Student> getStudentsByMajor(String major);
    void updateStudent(Student student);
    void deleteStudent(int studentId);
}

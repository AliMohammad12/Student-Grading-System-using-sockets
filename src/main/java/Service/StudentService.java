package Service;

import DAO.StudentDao;
import Model.Student;

import java.util.List;

public class StudentService {
    private StudentDao studentDao;

    public StudentService(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    public void createStudent(Student student) {
        studentDao.createStudent(student);
    }
    public Student getStudentById(int studentId) {
        return studentDao.getStudentById(studentId);
    }

    public List<Student> getAllStudents() {
        return studentDao.getAllStudents();
    }

    public List<Student> getStudentsByMajor(String major) {

        return studentDao.getStudentsByMajor(major);
    }

    public void updateStudent(Student student) {
        studentDao.updateStudent(student);
    }

    public void deleteStudent(int studentId) {
        studentDao.deleteStudent(studentId);
    }
}

package daoimpl;

import dao.StudentDao;
import model.Course;
import model.Instructor;
import model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDaoImpl implements StudentDao {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/student_grading_system_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public void createStudent(Student student) {
        String query = "INSERT INTO student (first_name, last_name, major, academic_year, email, account_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, student.getFirstName());
            statement.setString(2, student.getLastName());
            statement.setString(3, student.getMajor());
            statement.setInt(4, student.getAcademicYear());
            statement.setString(5, student.getEmail());
            statement.setInt(6, student.getAccountId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Sorry, we encountered an issue while creating your request. Please try again later.");
        }
    }
    public Student getStudentByAccountId(int accountId) {
        String query = "SELECT first_name, last_name, major, academic_year, email, id FROM student WHERE account_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, accountId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String major  = resultSet.getString("major");
                    int academicYear = resultSet.getInt("academic_year");
                    String email = resultSet.getString("email");
                    int studentId = resultSet.getInt("id");
                    Student student = new Student(firstName, lastName, major, academicYear, email, accountId);
                    student.setStudentId(studentId);
                    return student;
                }
            }
        } catch (SQLException e) {
            System.out.println("Sorry, we encountered an issue while creating your request. Please try again later.");
            e.printStackTrace();
        }
        return null;
    }
    public List<Student> findStudentsByCourseAndInstructor(Course course, Instructor instructor) {
        String query = "SELECT " +
                "s.id AS Student_ID, " +
                "s.first_name AS First_Name, " +
                "s.last_name AS Last_Name, " +
                "s.email AS Email, " +
                "s.major AS Major, " +
                "s.academic_year AS Academic_Year " +
                "FROM student s " +
                "JOIN student_courses sc ON s.id = sc.student_id " +
                "JOIN instructor_courses ic ON sc.instructor_id = ic.instructor_id " +
                "WHERE ic.instructor_id = ? AND sc.course_id = ?";

        List<Student> studentList = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, instructor.getInstructorId());
            statement.setInt(2, course.getCourseId());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int studentId = resultSet.getInt("Student_ID");
                    String firstName = resultSet.getString("First_Name");
                    String lastName = resultSet.getString("Last_Name");
                    String email = resultSet.getString("Email");
                    String major = resultSet.getString("Major");
                    int academicYear = resultSet.getInt("Academic_Year");

                    Student student = new Student(firstName, lastName, major, academicYear, email);
                    student.setStudentId(studentId);
                    studentList.add(student);
                }
            }
        } catch (SQLException e) {
            System.out.println("Sorry, we encountered an issue while creating your request. Please try again later.");
            e.printStackTrace();
        }
        return studentList;
    }
    public List<Student> getAllStudents() {
        String query = "select id, first_name, last_name, email, major, academic_year, account_id from student";
        List<Student> studentList = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String major = resultSet.getString("major");
                int academicYear = resultSet.getInt("academic_year");
                int accountId = resultSet.getInt("account_id");

                Student student = new Student(firstName, lastName, major, academicYear, email, accountId);
                student.setStudentId(id);
                studentList.add(student);
            }
        } catch (SQLException e) {
            System.out.println("Sorry, we encountered an issue while creating your request. Please try again later.");
            e.printStackTrace();
        }
        return studentList;
    }
    public Student getStudentById(int studentId) {
        return null;
    }

    public List<Student> getStudentsByMajor(String major) {
        return null;
    }

    public void updateStudent(Student student) {

    }

    public void deleteStudent(int studentId) {
        String query = "DELETE FROM student WHERE id = ?";
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, studentId);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

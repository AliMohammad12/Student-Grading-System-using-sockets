package daoimpl;

import dao.StudentDao;
import model.Student;

import java.sql.*;
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
            e.printStackTrace();
        }
        return null;
    }

    public Student getStudentById(int studentId) {
        return null;
    }

    public List<Student> getAllStudents() {
        return null;
    }

    public List<Student> getStudentsByMajor(String major) {
        return null;
    }

    public void updateStudent(Student student) {

    }

    public void deleteStudent(int studentId) {

    }
}

package daoimpl;


import dao.InstructorDao;
import model.Instructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class InstructorDaoImpl implements InstructorDao {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/student_grading_system_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public void createInstructor(Instructor instructor) {
        String query = "INSERT INTO instructor (first_name, last_name, department_name, email, account_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, instructor.getFirstName());
            statement.setString(2, instructor.getLastName());
            statement.setString(3, instructor.getDepartmentName());
            statement.setString(4, instructor.getEmail());
            statement.setInt(5, instructor.getAccountId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Sorry, we encountered an issue while creating your request. Please try again later.");
        }
    }

    public Instructor getInstructorById(int instructorId) {
        return null;
    }

    public List<Instructor> getAllInstructors() {
        return null;
    }

    public List<Instructor> getInstructorsByDepartment(int departmentId) {
        return null;
    }

    public void updateInstructor(Instructor instructor) {

    }

    public void deleteInstructor(int instructorId) {

    }
}

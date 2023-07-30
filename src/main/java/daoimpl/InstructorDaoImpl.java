package daoimpl;


import dao.InstructorDao;
import model.Department;
import model.Instructor;
import model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InstructorDaoImpl implements InstructorDao {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/student_grading_system_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public void createInstructor(Instructor instructor) {
        String query = "INSERT INTO instructor (first_name, last_name, department_id, email, account_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, instructor.getFirstName());
            statement.setString(2, instructor.getLastName());
            statement.setInt(3, instructor.getDepartment().getId());
            statement.setString(4, instructor.getEmail());
            statement.setInt(5, instructor.getAccountId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Sorry, we encountered an issue while creating your request. Please try again later.");
        }
    }
    public Instructor getInstructorByAccountId(int accountId) {
        String query = "SELECT i.first_name, i.last_name, i.department_id, i.email, i.id, d.department_name " +
                "FROM instructor i " +
                "JOIN department d ON i.department_id = d.id " +
                "WHERE i.account_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, accountId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String departmentName = resultSet.getString("department_name");
                    String email = resultSet.getString("email");
                    int instructorId = resultSet.getInt("id");
                    int departmentId = resultSet.getInt("department_id");

                    Department department = new Department(departmentName);
                    department.setId(departmentId);
                    Instructor instructor = new Instructor(firstName, lastName, department, email);
                    instructor.setAccountId(accountId);
                    instructor.setInstructorId(instructorId);
                    return instructor;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Instructor> getAllInstructors() {
        String query = "SELECT i.id, i.first_name, i.last_name, i.email, i.department_id, i.account_id, d.department_name " +
                "FROM instructor i " +
                "JOIN department d ON i.department_id = d.id";
        List<Instructor> instructorList = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                int departmentId = resultSet.getInt("department_id");
                int accountId = resultSet.getInt("account_id");
                String departmentName = resultSet.getString("department_name");
                Department department = new Department(departmentName);
                department.setId(departmentId);

                Instructor instructor = new Instructor(firstName, lastName, department, email);
                instructor.setAccountId(accountId);
                instructor.setInstructorId(id);

                instructorList.add(instructor);
            }
        } catch (SQLException e) {
            System.out.println("Sorry, we encountered an issue while creating your request. Please try again later.");
            e.printStackTrace();
        }
        return instructorList;
    }

    public Instructor getInstructorById(int instructorId) {
        return null;
    }
    public List<Instructor> getInstructorsByDepartment(int departmentId) {
        return null;
    }
    public void deleteInstructor(int instructorId) {
        String query = "DELETE FROM instructor WHERE id = ?";
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, instructorId);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

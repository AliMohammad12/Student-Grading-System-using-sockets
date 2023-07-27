package daoimpl;

import dao.DepartmentDao;
import model.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoImpl implements DepartmentDao {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/student_grading_system_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public void createDepartment(Department department) {

    }
    public Department getDepartmentById(int departmentId) {
        return null;
    }

    public List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();
        String query ="SELECT department_name FROM department";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("department_name");
                Department department = new Department(name);
                departments.add(department);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departments;
    }

    public void updateDepartment(Department department) {

    }
    public void deleteDepartment(int departmentId) {

    }
}

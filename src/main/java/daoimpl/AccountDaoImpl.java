package daoimpl;

import dao.AccountDao;
import model.Account;

import java.sql.*;


public class AccountDaoImpl implements AccountDao {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/student_grading_system_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public void createAccount(Account account) {
        String query = "INSERT INTO account (email, hashed_password, role) VALUES (?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, account.getEmail());
            statement.setString(2, account.getHashedPassword());
            statement.setString(3, account.getRole());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Sorry, we encountered an issue while creating your request. Please try again later.");
        }
    }

    public boolean emailExists(String username) {
        String query = "SELECT COUNT(*) FROM account WHERE email = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return false;
    }
    public Account getAccountByEmail(String email) {
        String query = "SELECT id, email, hashed_password, role FROM account WHERE email = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String userName = resultSet.getString("email");
                    String hashedPassword = resultSet.getString("hashed_password");
                    String role = resultSet.getString("role");

                    Account account = new Account(userName, hashedPassword, role);
                    account.setId(id);
                    return account;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getAccountIdByEmail(String email) {
        int accountId = -1;
        String query = "SELECT id FROM account WHERE email = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    accountId = resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accountId;
    }

    public void updateAccount(Account account) {

    }
    public void deleteAccount(int accountId) {
        String query = "DELETE FROM account WHERE id = ?";
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, accountId);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

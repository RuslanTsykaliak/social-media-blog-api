package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Account;


public class AccountDAO {

    private final Connection connection;

    public AccountDAO(Connection connection) {
        this.connection = connection;
    }

    public void create(Account account) throws SQLException {
        String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getPassword());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating account failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    account.setAccount_id(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating account failed, no ID obtained.");
                }
            }
        }
    }

    public Account read(int accountId) throws SQLException {
        String sql = "SELECT * FROM account WHERE account_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, accountId);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return mapResultSetToAccount(result);
                }
            }
        }
        return null;
    }

    public Account readByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM account WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return mapResultSetToAccount(result);
                }
            }
        }
        return null;
    }

    public void update(Account account) throws SQLException {
        String sql = "UPDATE account SET username = ?, password = ? WHERE account_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getPassword());
            statement.setInt(3, account.getAccount_id());
            statement.executeUpdate();
        }
    }

    public void delete(Account account) throws SQLException {
        String sql = "DELETE FROM account WHERE account_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, account.getAccount_id());
            statement.executeUpdate();
        }
    }

    public List<Account> getAll() throws SQLException {
        String sql = "SELECT * FROM account";
        List<Account> accounts = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                accounts.add(mapResultSetToAccount(result));
            }
        }
        return accounts;
    }

    private Account mapResultSetToAccount(ResultSet result) throws SQLException {
        int accountId = result.getInt("account_id");
        String username = result.getString("username");
        String password = result.getString("password");
        return new Account(accountId, username, password);
    }
}
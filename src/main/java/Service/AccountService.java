package Service;

import DAO.AccountDAO;
import Model.Account;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class AccountService {

    private final AccountDAO accountDAO;

    public AccountService(Connection connection) {
        this.accountDAO = new AccountDAO(connection);
    }

    public void create(Account account) throws SQLException {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }
        accountDAO.create(account);
    }

    public Account read(int accountId) throws SQLException {
        return accountDAO.read(accountId);
    }

    public Account readByUsername(String username) throws SQLException {
        return accountDAO.readByUsername(username);
    }

    public void update(Account account) throws SQLException {
        if (account == null || account.getAccount_id() <= 0) {
            throw new IllegalArgumentException("Invalid account object for update");
        }
        accountDAO.update(account);
    }

    public void delete(Account account) throws SQLException {
        if (account == null || account.getAccount_id() <= 0) {
            throw new IllegalArgumentException("Invalid account object for deletion");
        }
        accountDAO.delete(account);
    }

    public List<Account> getAll() throws SQLException {
        return accountDAO.getAll();
    }
}